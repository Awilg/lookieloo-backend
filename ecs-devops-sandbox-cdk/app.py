#!/usr/bin/env python3

from aws_cdk import core

from ecs_devops_sandbox_cdk.ecs_devops_sandbox_cdk_stack import EcsDevopsSandboxCdkStack


app = core.App()
EcsDevopsSandboxCdkStack(app, "ecs-devops-sandbox-cdk")

app.synth()
