package com.atlassian.performance.tools.awsinfrastructure

import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.regions.Regions
import com.atlassian.performance.tools.aws.api.Aws
import com.atlassian.performance.tools.workspace.api.RootWorkspace
import org.apache.logging.log4j.core.config.ConfigurationFactory
import java.time.Duration
import java.util.function.Predicate

object IntegrationTestRuntime {
    val taskWorkspace = RootWorkspace().currentTask
    val aws: Aws

    init {
        ConfigurationFactory.setConfigurationFactory(LogConfigurationFactory(taskWorkspace))
        aws = Aws.Builder(Regions.EU_WEST_1)
            .credentialsProvider(AWSCredentialsProviderChain(listOf(EnvironmentVariableCredentialsProvider(), EC2ContainerCredentialsProviderWrapper())))
            .availabilityZoneFilter(Predicate { it.zoneName in listOf("eu-west-1a", "eu-west-1c") })
            .regionsWithHousekeeping(listOf(Regions.EU_WEST_1))
            .batchingCloudformationRefreshPeriod(Duration.ofSeconds(15))
            .build()
    }
}
