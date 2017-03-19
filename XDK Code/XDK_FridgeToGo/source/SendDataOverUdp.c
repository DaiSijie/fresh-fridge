/**
* Licensee agrees that the example code provided to Licensee has been developed and released by Bosch solely as an example to be used as a potential reference for Licensee�s application development. 
* Fitness and suitability of the example code for any use within Licensee�s applications need to be verified by Licensee on its own authority by taking appropriate state of the art actions and measures (e.g. by means of quality assurance measures).
* Licensee shall be responsible for conducting the development of its applications as well as integration of parts of the example code into such applications, taking into account the state of the art of technology and any statutory regulations and provisions applicable for such applications. Compliance with the functional system requirements and testing there of (including validation of information/data security aspects and functional safety) and release shall be solely incumbent upon Licensee. 
* For the avoidance of doubt, Licensee shall be responsible and fully liable for the applications and any distribution of such applications into the market.
* 
* 
* Redistribution and use in source and binary forms, with or without 
* modification, are permitted provided that the following conditions are 
* met:
* 
*     (1) Redistributions of source code must retain the above copyright
*     notice, this list of conditions and the following disclaimer. 
* 
*     (2) Redistributions in binary form must reproduce the above copyright
*     notice, this list of conditions and the following disclaimer in
*     the documentation and/or other materials provided with the
*     distribution.  
*     
*     (3)The name of the author may not be used to
*     endorse or promote products derived from this software without
*     specific prior written permission.
* 
*  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR 
*  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
*  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
*  DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
*  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
*  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
*  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
*  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
*  STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
*  IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
*  POSSIBILITY OF SUCH DAMAGE.
*/
//lint -esym(956,*) /* Suppressing "Non const, non volatile static or external variable" lint warning*/

/* module includes ********************************************************** */

/* system header files */
#include <stdio.h>
/* additional interface header files */
#include "simplelink.h"
#include "BCDS_Basics.h"
#include "BCDS_Assert.h"
#include "FreeRTOS.h"
#include "timers.h"
#include "led.h"
#include "BCDS_WlanConnect.h"
#include "BCDS_NetworkConfig.h"
#include <Serval_Types.h>
#include <Serval_Basics.h>
#include <Serval_Ip.h>
/* own header files */
#include "SendDataOverUdp.h"


#include "XdkBoardHandle.h"

/* constant definitions ***************************************************** */

/* local variables ********************************************************** */

// STATIC VARIABLES
static LED_handle_tp redLedHandle = (LED_handle_tp) NULL;
static LED_handle_tp yellowLedHandle = (LED_handle_tp) NULL;
static LED_handle_tp orangeLedHandle = (LED_handle_tp) NULL;

/**
 * This buffer holds the data to be sent to server via UDP
 * */
static uint16_t bsdBuffer_mau[BUFFER_SIZE] = { (uint16_t) ZERO };
/**
 * Timer handle for connecting to wifi and obtaining the IP address
 */
xTimerHandle wifiConnectTimerHandle_gdt = NULL;
/**
 * Timer handle for periodically sending data over wifi
 */
xTimerHandle wifiSendTimerHandle = NULL;
xTimerHandle wifiReceiveTimerHandle = NULL;

/* global variables ********************************************************* */

/* inline functions ********************************************************* */

/* local functions ********************************************************** */
/**
 *  @brief
 *      Function to initialize the wifi network send application. Create timer task
 *      to start WiFi Connect and get IP function after one second. After that another timer
 *      to send data periodically.
 */
void init(void)
{
    uint32_t Ticks_send = UINT32_C(3000);
    uint32_t Ticks_recv = UINT32_C(1000);

    if (Ticks_send != UINT32_MAX) /* Validated for portMAX_DELAY to assist the task to wait Infinitely (without timing out) */
    {
        Ticks_send /= portTICK_RATE_MS;
    }
    if (UINT32_C(0) == Ticks_send) /* ticks cannot be 0 in FreeRTOS timer. So ticks is assigned to 1 */
    {
        Ticks_send = UINT32_C(1);
    }
    if (Ticks_recv != UINT32_MAX) /* Validated for portMAX_DELAY to assist the task to wait Infinitely (without timing out) */
        {
            Ticks_recv /= portTICK_RATE_MS;
        }
        if (UINT32_C(0) == Ticks_recv) /* ticks cannot be 0 in FreeRTOS timer. So ticks is assigned to 1 */
        {
            Ticks_recv = UINT32_C(1);
        }
    /* create timer task*/
    wifiConnectTimerHandle_gdt = xTimerCreate((char * const ) "wifiConnect", Ticks_recv, TIMER_AUTORELOAD_OFF, NULL, wifiConnectGetIP);
    wifiSendTimerHandle = xTimerCreate((char * const ) "wifiSend", Ticks_send, TIMER_AUTORELOAD_ON, NULL, wifiSend);
    //wifiReceiveTimerHandle = xTimerCreate((char * const ) "wifiReceive", Ticks_recv, TIMER_AUTORELOAD_ON, NULL, wifiReceive);

    if ((wifiConnectTimerHandle_gdt != NULL) && (wifiSendTimerHandle != NULL))
    {
        /*start the wifi connect timer*/
        if ( xTimerStart(wifiConnectTimerHandle_gdt, TIMERBLOCKTIME) != pdTRUE)
        {
            assert(false);
        }
    }
    else
    {
        /* Assertion Reason: "Failed to create timer task during initialization"   */
        assert(false);
    }

    redLedHandle = LED_create(gpioRedLed_Handle, GPIO_STATE_OFF);
    yellowLedHandle = LED_create(gpioYellowLed_Handle, GPIO_STATE_OFF);
    orangeLedHandle = LED_create(gpioOrangeLed_Handle, GPIO_STATE_OFF);
   	environmentalSensorInit();


    LED_setState(redLedHandle, LED_SET_OFF);
   	LED_setState(orangeLedHandle, LED_SET_OFF);
   	LED_setState(yellowLedHandle, LED_SET_ON);
}

/**
 *  @brief API to Deinitialize the PSD module
 */
void deinit(void)
{
    environmentalSensorDeinit();
}

/**
 * @brief This is a template function where the user can write his custom application.
 *
 */
void appInitSystem(xTimerHandle xTimer)
{
    BCDS_UNUSED(xTimer);
    /*Call the WNS module init API */
    init();
}

/**
 * @brief Opening a UDP client side socket and sending data on a server port
 *
 * This function opens a UDP socket and tries to connect to a Server SERVER_IP
 * waiting on port SERVER_PORT.
 * Then the function will send periodic UDP packets to the server.
 * 
 * @param[in] port
 *					destination port number
 *
 * @return         returnTypes_t:
 *                                  SOCKET_ERROR: when socket has not opened properly
 *                                  SEND_ERROR: when 0 transmitted bytes or send error
 *                                  STATUS_OK: when UDP sending was successful
 */


static returnTypes_t bsdUdpServer(uint16_t port){/*
		static uint16_t counter = UINT16_C(0);
		uint16_t bsdBuffer[BUFFER_SIZE] = { (uint16_t) ZERO };
	 // RECEIVING SOCKET
	    SlSockAddrIn_t Addr;
	    SlSockAddrIn_t  LocalAddr;
	    uint16_t AddrSize = (uint16_t) ZERO;
	    int16_t SockID = (int16_t) ZERO;
	    int16_t Status = (int16_t) ZERO;
	    Addr.sin_family = SL_AF_INET;
	    Addr.sin_port = sl_Htons((uint16_t) port);
	    Addr.sin_addr.s_addr = sl_Htonl(SERVER_IP);
	    AddrSize = sizeof(SlSockAddrIn_t);

	    SockID = sl_Socket(SL_AF_INET, SL_SOCK_DGRAM, (uint32_t) ZERO); /**<The return value is a positive number if successful; other wise negative*/
	     /*   if (SockID < (int16_t) ZERO)
	        {
	            /* error case*//*
	            return (SOCKET_ERROR);
	        }
	        printf("test\n");
	        Status = sl_Bind(SockID, (SlSockAddr_t *)&Addr, AddrSize);
	        Status = sl_RecvFrom(SockID, &bsdBuffer, BUFFER_SIZE * sizeof(uint16_t), (uint32_t) ZERO, (SlSockAddr_t *) &Addr, AddrSize);/**<The return value is a number of characters sent;negative if not successful*/
	            /*Check if 0 transmitted bytes sent or error condition*/
	SlSockAddrIn_t  Addr;
	SlSockAddrIn_t  LocalAddr;
	_i16 AddrSize = sizeof(SlSockAddrIn_t);
	_i16 SockID;
	_i16 Status;
	_i8 Buf[BUFFER_SIZE];

	struct SlTimeval_t timeVal;

	timeVal.tv_sec = 3; // Seconds

	timeVal.tv_usec = 0; // Microseconds. 10000 microseconds resolution

	LocalAddr.sin_family = SL_AF_INET;
	LocalAddr.sin_port = sl_Htons(port);
	LocalAddr.sin_addr.s_addr = 0;

	SockID = sl_Socket(SL_AF_INET,SL_SOCK_DGRAM, 0);
	Status = sl_Bind(SockID, (SlSockAddr_t *)&LocalAddr, AddrSize);

	sl_SetSockOpt(SockID,SL_SOL_SOCKET,SL_SO_RCVTIMEO, (_u8 *)&timeVal, sizeof(timeVal));
	Status = sl_RecvFrom(SockID, Buf, 1472, 0, (SlSockAddr_t *)&Addr, (SlSocklen_t*)&AddrSize);
	            if (Status <= (int16_t) ZERO)
	            {
	            }
	            else{
	            	//printf("%s\n", Buf);
	            	if(Buf[0]=='0'){
	            		//printf("Red led on\n");
	            		LED_setState(redLedHandle, LED_SET_ON);
	            		LED_setState(orangeLedHandle, LED_SET_OFF);
	            		LED_setState(yellowLedHandle, LED_SET_OFF);
	            	}
	            	else if(Buf[0]=='1'){
	            		//printf("Orange led on\n");
	            		LED_setState(redLedHandle, LED_SET_OFF);
	            		LED_setState(orangeLedHandle, LED_SET_ON);
	            		LED_setState(yellowLedHandle, LED_SET_OFF);
	            	}
	            	else if(Buf[0]=='2'){
	            		//printf("Green led on\n");
	            		LED_setState(redLedHandle, LED_SET_OFF);
	            		LED_setState(orangeLedHandle, LED_SET_OFF);
	            		LED_setState(yellowLedHandle, LED_SET_ON);
	            	}
	            }
	            Status = sl_Close(SockID);
	                if (Status < 0)
	                {
	                    return (SEND_ERROR);
	                }
	            return (STATUS_OK);
}

static returnTypes_t bsdUdpClient(uint16_t port)
{
    static uint16_t counter = UINT16_C(0);
    // SENDING SOCKET
    SlSockAddrIn_t Addr;
    uint16_t AddrSize = (uint16_t) ZERO;
    int16_t SockID = (int16_t) ZERO;
    int16_t Status = (int16_t) ZERO;
    /* copies the dummy data to send array , the first array element is the running counter to track the number of packets send so far*/
   /* bsdBuffer_mau[0] = (uint16_t) 'ta';
    bsdBuffer_mau[1] = (uint16_t) 'el';
    bsdBuffer_mau[2] = (uint16_t) 'sa';
    bsdBuffer_mau[3] = (uint16_t) 'ta';
    */
    Addr.sin_family = SL_AF_INET;
    Addr.sin_port = sl_Htons((uint16_t) port);
    Addr.sin_addr.s_addr = sl_Htonl(SERVER_IP);
    AddrSize = sizeof(SlSockAddrIn_t);

    SockID = sl_Socket(SL_AF_INET, SL_SOCK_DGRAM, (uint32_t) ZERO); /**<The return value is a positive number if successful; other wise negative*/
    if (SockID < (int16_t) ZERO)
    {
        /* error case*/
    	printf("SOCKET ERROR 1 !!!\n");
        return (SOCKET_ERROR);
    }

    Status = sl_SendTo(SockID, bsdBuffer_mau, BUFFER_SIZE * sizeof(uint16_t), (uint32_t) ZERO, (SlSockAddr_t *) &Addr, AddrSize);/**<The return value is a number of characters sent;negative if not successful*/
    /*Check if 0 transmitted bytes sent or error condition*/
    if (Status <= (int16_t) ZERO)
    {
        Status = sl_Close(SockID);
        if (Status < 0)
        {
        	printf("SOCKET ERROR 2 !!!\n");
            return (SEND_ERROR);
        }
        printf("SOCKET ERROR 3 !!!\n");
        return (SEND_ERROR);
    }
    Status = sl_Close(SockID);
    if (Status < 0)
    {
    	printf("SOCKET ERROR 4 !!!\n");
        return (SEND_ERROR);
    }
    counter++;
    printf("SUCCESS ! %u\n", counter);
    return (STATUS_OK);
}
/**
 *  @brief
 *      Function to periodically send data over WiFi as UDP packets. This is run as an Auto-reloading timer.
 *
 *  @param [in ] xTimer - necessary parameter for timer prototype
 */
static void wifiSend(xTimerHandle xTimer)
{
    BCDS_UNUSED(xTimer);

    if (STATUS_OK != bsdUdpServer(CLIENT_PORT))
        {
            /* assertion Reason:  "Failed to  receive udp packet" */
            assert(false);
        }
    if (STATUS_OK != bsdUdpClient(SERVER_PORT))
        {
            /* assertion Reason:  "Failed to  send udp packet" */
            assert(false);
        }
}
/*
static void wifiReceive(xTimerHandle xTimer)
{
    BCDS_UNUSED(xTimer);
    if (STATUS_OK != bsdUdpServer(CLIENT_PORT))
    {
        /* assertion Reason:  "Failed to  receive udp packet" *//*
        assert(false);
    }
}*/

/**
 *  @brief
 *      Function to connect to wifi network and obtain IP address
 *
 *  @param [in ] xTimer
 */
static void wifiConnectGetIP(xTimerHandle xTimer)
{
    BCDS_UNUSED(xTimer);

    NetworkConfig_IpSettings_T myIpSettings;
    memset(&myIpSettings, (uint32_t) 0, sizeof(myIpSettings));
    char ipAddress[PAL_IP_ADDRESS_SIZE] = { 0 };
    Ip_Address_T* IpaddressHex = Ip_getMyIpAddr();
    WlanConnect_SSID_T connectSSID;
    WlanConnect_PassPhrase_T connectPassPhrase;
    Retcode_T ReturnValue = (Retcode_T)RETCODE_FAILURE;
    int32_t Result = INT32_C(-1);

    if (RETCODE_OK != WlanConnect_Init())
    {
        printf("Error occurred initializing WLAN \r\n ");
        return;
    }

    printf("Connecting to %s \r\n ", WLAN_CONNECT_WPA_SSID);

    connectSSID = (WlanConnect_SSID_T) WLAN_CONNECT_WPA_SSID;
    connectPassPhrase = (WlanConnect_PassPhrase_T) WLAN_CONNECT_WPA_PASS;
    ReturnValue = NetworkConfig_SetIpDhcp(NULL);
    if (ReturnValue)
    {
        printf("Error in setting IP to DHCP\n\r");
        return;
    }

    if(RETCODE_OK == WlanConnect_WPA(connectSSID, connectPassPhrase, NULL))
    {
        ReturnValue = NetworkConfig_GetIpSettings(&myIpSettings);
        if (RETCODE_OK == ReturnValue)
        {
            *IpaddressHex = Basics_htonl(myIpSettings.ipV4);
            Result = Ip_convertAddrToString(IpaddressHex, ipAddress);
            if (Result < 0)
            {
                printf("Couldn't convert the IP address to string format \r\n ");
                return;
            }
            printf("Connected to WPA network successfully. \r\n ");
            printf(" Ip address of the device: %s \r\n ", ipAddress);
        }
        else
        {
            printf("Error in getting IP settings\n\r");
            return;
        }
    }
    else
    {
        printf("Error occurred connecting %s \r\n ", WLAN_CONNECT_WPA_SSID);
        return;
    }

    /* After connection start the wifi sending timer*/
    if (xTimerStart(wifiSendTimerHandle, TIMERBLOCKTIME) != pdTRUE)
    {
        assert(false);
    }
    /* After connection start the wifi receiving timer*/
    /*    if (xTimerStart(wifiReceiveTimerHandle, TIMERBLOCKTIME) != pdTRUE)
        {
            assert(false);
        }*/
}

/* global functions ********************************************************* */

void setMessage(char* message, size_t size){
	for(size_t i = 0; i < size/2 ; ++i){
		bsdBuffer_mau[i] = (uint16_t) (message[2*i]+message[2*i+1]*256);
	}
}

/** ************************************************************************* */
