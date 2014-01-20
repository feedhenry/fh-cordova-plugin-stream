//
//  CDVViewController+FirstResponder.m
//  Test
//
//  Created by Wei Li on 20/01/2014.
//
//

#import "CDVViewController+FirstResponder.h"

@implementation CDVViewController (FirstResponder)

@dynamic responder;

- (BOOL)canBecomeFirstResponder {
	return YES;
}

- (void)remoteControlReceivedWithEvent:(UIEvent *)event {
  if(self.responder){
    SEL remoteSelector = @selector(receiveRemoteControlEvents:);
    if([self.responder respondsToSelector:remoteSelector]){
      [self.responder performSelector:remoteSelector withObject:event];
    }
  }
}

@end
