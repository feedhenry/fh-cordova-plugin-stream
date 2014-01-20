//
//  CDVViewController+FirstResponder.h
//  Test
//
//  Created by Wei Li on 20/01/2014.
//
//

#import <Cordova/CDVViewController.h>

@interface CDVViewController (FirstResponder)

@property (nonatomic, strong) CDVPlugin * responder;
- (void) setResponder:(CDVPlugin*) responder;

@end
