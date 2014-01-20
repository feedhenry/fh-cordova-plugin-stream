//
//  CDVViewController+FirstResponder.m
//  Test
//
//  Created by Wei Li on 20/01/2014.
//
//

#import "CDVViewController+FirstResponder.h"
#import <objc/runtime.h>

@implementation CDVViewController (FirstResponder)

static char KEY;

- (void) setResponder:(CDVPlugin*) aResponder
{
  objc_setAssociatedObject(self, &KEY, aResponder, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (BOOL)canBecomeFirstResponder {
  return YES;
}

- (void)remoteControlReceivedWithEvent:(UIEvent *)event {
  id responder = objc_getAssociatedObject(self, &KEY);
  if(responder){
    SEL remoteSelector = @selector(receiveRemoteControlEvents:);
    if([responder respondsToSelector:remoteSelector]){
      [responder performSelector:remoteSelector withObject:event];
    }
  }
}

@end
