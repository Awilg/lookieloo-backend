"""AWS CDK module to create ECS infrastructure"""
from aws_cdk import (core, aws_ecs as ecs, aws_ecr as ecr, aws_ec2 as ec2, aws_iam as iam, aws_ecs_patterns as ecs_patterns)

class LookieLooCdkStack(core.Stack):

    def __init__(self, scope: core.Construct, id: str, **kwargs) -> None:
        super().__init__(scope, id, **kwargs)

        # Create the ECR Repository
        ecr_repository = ecr.Repository(self,
                                        "lookieloo-repository",
                                        repository_name="lookieloo")

        # Create the ECS Cluster (and VPC)
        vpc = ec2.Vpc(self,
                      "lookieloo-vpc",
                      max_azs=3)
        cluster = ecs.Cluster(self,
                              "lookieloo-cluster",
                              cluster_name="lookieloo-cluster",
                              vpc=vpc)

        # Create the ECS Task Definition with placeholder container (and named Task Execution IAM Role)
        execution_role = iam.Role(self,
                                  "lookieloo-execution-role",
                                  assumed_by=iam.ServicePrincipal("ecs-tasks.amazonaws.com"),
                                  role_name="lookieloo-execution-role")
        execution_role.add_to_policy(iam.PolicyStatement(
            effect=iam.Effect.ALLOW,
            resources=["*"],
            actions=[
                "ecr:GetAuthorizationToken",
                "ecr:BatchCheckLayerAvailability",
                "ecr:GetDownloadUrlForLayer",
                "ecr:BatchGetImage",
                "logs:CreateLogStream",
                "logs:PutLogEvents"
                ]
        ))
        task_definition = ecs.FargateTaskDefinition(self,
                                                    "lookieloo-task-definition",
                                                    execution_role=execution_role,
                                                    family="lookieloo-task-definition")
        container = task_definition.add_container(
            "lookieloo-container",
            image=ecs.ContainerImage.from_registry("amazon/amazon-ecs-sample")
        )

        port_mapping = ecs.PortMapping(
            container_port=80,
            protocol=ecs.Protocol.TCP
        )

        container.add_port_mappings(port_mapping)

        # Create the ECS Service
        fargate_service = ecs_patterns.ApplicationLoadBalancedFargateService(self,
                                     "lookieloo-service",
                                     cluster=cluster,
                                     task_definition=task_definition,
                                     service_name="lookieloo-service")

        fargate_service.service.connections.security_groups[0].add_ingress_rule(
            peer = ec2.Peer.ipv4(vpc.vpc_cidr_block),
            connection = ec2.Port.tcp(80),
            description="Allow http inbound from VPC"
        )

        core.CfnOutput(
            self, "LoadBalancerDNS",
            value=fargate_service.load_balancer.load_balancer_dns_name
        )
