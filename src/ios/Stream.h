#import <Foundation/Foundation.h>

#import <Cordova/CDVPlugin.h>
#import "AudioStreamer.h"
#import "CDVViewController+FirstResponder.h"

@interface Stream : CDVPlugin
{
    NSString *successCallback;
    NSString *errorCallback;
    AudioStreamer *streamer;
    NSString* currentContentUrl;
}

@property (retain) NSString* successCallback;
@property (retain) NSString* errorCallback;
@property (retain) NSString* currentContentUrl;

- (void) play:(CDVInvokedUrlCommand*)command;
- (void) stop:(CDVInvokedUrlCommand*)command;
- (void) pause:(CDVInvokedUrlCommand*)command;
- (void)receiveRemoteControlEvents:(UIEvent *)event;


@end