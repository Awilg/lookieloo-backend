#!/usr/bin/env python3

from aws_cdk import core

from lookieloo_cdk.lookieloo_cdk_stack import LookieLooCdkStack


app = core.App()
LookieLooCdkStack(app, "lookieloo-cdk")

app.synth()
