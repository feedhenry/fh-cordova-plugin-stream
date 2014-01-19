#import "Stream.h"
#import <Cordova/CDVViewController.h>

@implementation Stream

@synthesize successCallback, errorCallback, currentContentUrl;

- (void) play:(CDVInvokedUrlCommand*)command
{
    UIApplication *application = [UIApplication sharedApplication];
	if([application respondsToSelector:@selector(beginReceivingRemoteControlEvents)]){
        [application beginReceivingRemoteControlEvents];
    }
    CDVViewController* viewController = (CDVViewController*)[self viewController];
    viewController.responder = self;
    [viewController becomeFirstResponder];
    
    NSDictionary *options = [command.arguments objectAtIndex:0];
    NSString *url = [options objectForKey:@"path"];
    NSURL *contentUrl = nil;
    
    NSLog(@"Stream Url %@", url);
    
    if(nil != currentContentUrl && nil != streamer){
        if ([currentContentUrl isEqualToString:url] || [url isEqualToString:@""]) {
            if(nil != streamer){
                [streamer start];
            }
            NSString* jscallback = [NSString stringWithFormat:@"window.plugins.stream.play_success()"];
            [self writeJavascript:jscallback];
            return;            
        }
    }
    
    self.currentContentUrl = url;
    
    contentUrl = [NSURL URLWithString: url];
    streamer = [[AudioStreamer alloc] initWithURL:contentUrl];
    [streamer start];
    
    NSString* jscallback = [NSString stringWithFormat:@"window.plugins.stream.play_success()"];
    [self writeJavascript:jscallback];
}

- (void) pause:(CDVInvokedUrlCommand*)command
{
    NSLog(@"pause");
    if(nil != streamer){
        [streamer pause];
        NSString* jscallback = [NSString stringWithFormat:@"window.plugins.stream.pause_success()"];
        [self writeJavascript:jscallback];
    } else {
        NSString* jscallback = [NSString stringWithFormat:@"window.plugins.stream.pause_error('no_audio_playing')"];
        [self writeJavascript:jscallback];
    }
    
}

- (void) stop:(CDVInvokedUrlCommand*)command
{
    NSLog(@"stop");
    if(nil != streamer){
        [self destroyStream];
        NSString* jscallback = [NSString stringWithFormat:@"window.plugins.stream.stop_success()"];
        [self writeJavascript:jscallback];
    } else {
        NSString* jscallback = [NSString stringWithFormat:@"window.plugins.stream.stop_error('no_audio_playing')"];
        [self writeJavascript:jscallback];
    }
}

- (void)receiveRemoteControlEvents:(UIEvent *)event {
    switch (event.subtype) {
		case UIEventSubtypeRemoteControlTogglePlayPause:
			[streamer pause];
			break;
		case UIEventSubtypeRemoteControlPlay:
			[streamer start];
			break;
		case UIEventSubtypeRemoteControlPause:
			[streamer pause];
			break;
		case UIEventSubtypeRemoteControlStop:
			[streamer stop];
			break;
		default:
			break;
	}
}

-(void) destroyStream {
    if(streamer){
        [streamer stop];
        streamer = nil;
    }
}

-(void) dealloc {
  
    [self destroyStream];

}

@end

