package com.github.chriskn.structurizrextension.plantuml

import java.net.MalformedURLException
import java.net.URL

internal const val AWS_ICON_URL = "https://raw.githubusercontent.com/awslabs/aws-icons-for-plantuml/v11.1/dist/"
internal const val AWS_ICON_COMMONS = "${AWS_ICON_URL}AWSCommon.puml"

/**
 * Registry containing the available icons.
 *
 * Allows to register icon url by icon name (case-insensitive) and to access icon url by name.
 * Urls have to be well-formed and need to point to .puml files.
 */
object IconRegistry {

    private const val GILBARBARA_ICON_URL =
        "https://raw.githubusercontent.com/plantuml-stdlib/gilbarbara-plantuml-sprites/master/sprites/"
    private const val PUML_FILE_EXTENSION = ".puml"

    private val iconNameToIconUrl = mutableMapOf<String, URL>()

    /**
     * @return The URL of an icon with the given name (case-insensitive) or null if no icon with the given name exists.
     */
    internal fun iconUrlFor(name: String): String? = iconNameToIconUrl[name.lowercase()]?.toString()

    /**
     * @return The file name of an icon with the given name (case-insensitive) or null if no icon with the given name exists.
     */
    internal fun iconFileNameFor(name: String?): String? {
        return if (name == null || !exists(name)) {
            null
        } else {
            iconNameToIconUrl[name.lowercase()]
                .toString()
                .split("/")
                .last()
                .replace(PUML_FILE_EXTENSION, "")
        }
    }

    /**
     * Adds a new icon with the given name (case-insensitive) and URL to the registry.
     *
     * @throws IllegalArgumentException if url does not point to puml file
     * @throws MalformedURLException if url is invalid
     */
    fun addIcon(name: String, url: String) {
        require(url.endsWith(PUML_FILE_EXTENSION)) {
            "Icon URL needs to point to .puml file"
        }
        iconNameToIconUrl[name.lowercase()] = URL(url)
    }

    internal fun reset() {
        iconNameToIconUrl.clear()
        iconNameToIconUrl.putAll(commonIcons.mapValues { URL(it.value) })
        iconNameToIconUrl.putAll(awsIcons.mapValues { URL(it.value) })
    }

    /**
     * @return True if an icon with the given name (case-insensitive) exists, false otherwise.
     */
    fun exists(name: String): Boolean = iconNameToIconUrl.containsKey(name.lowercase())

    private val commonIcons = mapOf(
        "kotlin" to "${GILBARBARA_ICON_URL}kotlin.puml",
        "android" to "${GILBARBARA_ICON_URL}android-icon.puml",
        "docker" to "${GILBARBARA_ICON_URL}docker-icon.puml",
        "github" to "${GILBARBARA_ICON_URL}github-icon.puml",
        "html5" to "${GILBARBARA_ICON_URL}html-5.puml",
        "intellij" to "${GILBARBARA_ICON_URL}intellij-idea.puml",
        "javascript" to "${GILBARBARA_ICON_URL}javascript.puml",
        "jira" to "${GILBARBARA_ICON_URL}jira.puml",
        "nodejs" to "${GILBARBARA_ICON_URL}nodejs-icon.puml",
        "npm" to "${GILBARBARA_ICON_URL}npm.puml",
        "nginx" to "${GILBARBARA_ICON_URL}nginx.puml",
        "mongodb" to "${GILBARBARA_ICON_URL}mongodb.puml",
        "mysql" to "${GILBARBARA_ICON_URL}mysql.puml",
        "postgresql" to "${GILBARBARA_ICON_URL}postgresql.puml",
        "react" to "${GILBARBARA_ICON_URL}react.puml",
        "redis" to "${GILBARBARA_ICON_URL}redis.puml",
        "kafka" to "${GILBARBARA_ICON_URL}kafka.puml",
        "tomcat" to "${GILBARBARA_ICON_URL}tomcat.puml",
        "apple" to "${GILBARBARA_ICON_URL}apple.puml",
        "ios" to "${GILBARBARA_ICON_URL}ios.puml",
        "kubernetes" to "${GILBARBARA_ICON_URL}kubernetes.puml",
        "kibana" to "${GILBARBARA_ICON_URL}kibana.puml",
        "grafana" to "${GILBARBARA_ICON_URL}grafana.puml",
        "gradle" to "${GILBARBARA_ICON_URL}gradle.puml",
        "sonarqube" to "${GILBARBARA_ICON_URL}sonarqube.puml",
        "prometheus" to "${GILBARBARA_ICON_URL}prometheus.puml",
        "swagger" to "${GILBARBARA_ICON_URL}swagger.puml",
        "springboot" to "${GILBARBARA_ICON_URL}spring.puml",
        "graphql" to "${GILBARBARA_ICON_URL}graphql.puml",
        "apple" to "${GILBARBARA_ICON_URL}apple.puml",
        "rocksdb" to "${GILBARBARA_ICON_URL}rocksdb.puml"
    )

    private val awsIcons = mapOf(
        "awsapigateway" to "${AWS_ICON_URL}ApplicationIntegration/APIGateway.puml",
        "awsapigatewayendpoint" to "${AWS_ICON_URL}ApplicationIntegration/APIGatewayEndpoint.puml",
        "awsappflow" to "${AWS_ICON_URL}ApplicationIntegration/AppFlow.puml",
        "awsappsync" to "${AWS_ICON_URL}ApplicationIntegration/AppSync.puml",
        "awsapplicationintegration" to "${AWS_ICON_URL}ApplicationIntegration/ApplicationIntegration.puml",
        "awsconsolemobileapplication" to "${AWS_ICON_URL}ApplicationIntegration/ConsoleMobileApplication.puml",
        "awseventbridge" to "${AWS_ICON_URL}ApplicationIntegration/EventBridge.puml",
        "awseventbridgecustomeventbus" to "${AWS_ICON_URL}ApplicationIntegration/EventBridgeCustomEventBus.puml",
        "awseventbridgedefaulteventbus" to "${AWS_ICON_URL}ApplicationIntegration/EventBridgeDefaultEventBus.puml",
        "awseventbridgeevent" to "${AWS_ICON_URL}ApplicationIntegration/EventBridgeEvent.puml",
        "awseventbridgerule" to "${AWS_ICON_URL}ApplicationIntegration/EventBridgeRule.puml",
        "awseventbridgesaaspartnerevent" to "${AWS_ICON_URL}ApplicationIntegration/EventBridgeSaasPartnerEvent.puml",
        "awseventbridgeschema" to "${AWS_ICON_URL}ApplicationIntegration/EventBridgeSchema.puml",
        "awseventbridgeschemaregistry" to "${AWS_ICON_URL}ApplicationIntegration/EventBridgeSchemaRegistry.puml",
        "awsexpressworkflows" to "${AWS_ICON_URL}ApplicationIntegration/ExpressWorkflows.puml",
        "awsmq" to "${AWS_ICON_URL}ApplicationIntegration/MQ.puml",
        "awsmqbroker" to "${AWS_ICON_URL}ApplicationIntegration/MQBroker.puml",
        "awsmanagedworkflowsforapacheairflow" to "${AWS_ICON_URL}ApplicationIntegration/ManagedWorkflowsforApacheAirflow.puml",
        "awssimplenotificationservice" to "${AWS_ICON_URL}ApplicationIntegration/SimpleNotificationService.puml",
        "awssimplenotificationserviceemailnotification" to "${AWS_ICON_URL}ApplicationIntegration/SimpleNotificationServiceEmailNotification.puml",
        "awssimplenotificationservicehttpnotification" to "${AWS_ICON_URL}ApplicationIntegration/SimpleNotificationServiceHTTPNotification.puml",
        "awssimplenotificationservicetopic" to "${AWS_ICON_URL}ApplicationIntegration/SimpleNotificationServiceTopic.puml",
        "awssqs" to "${AWS_ICON_URL}ApplicationIntegration/SimpleQueueService.puml",
        "awssqsmessage" to "${AWS_ICON_URL}ApplicationIntegration/SimpleQueueServiceMessage.puml",
        "awssqsqueue" to "${AWS_ICON_URL}ApplicationIntegration/SimpleQueueServiceQueue.puml",
        "awsstepfunctions" to "${AWS_ICON_URL}ApplicationIntegration/StepFunctions.puml",
        "awsapprunner" to "${AWS_ICON_URL}Compute/AppRunner.puml",
        "awsapplicationautoscaling" to "${AWS_ICON_URL}Compute/ApplicationAutoScaling.puml",
        "awsbatch" to "${AWS_ICON_URL}Compute/Batch.puml",
        "awsbottlerocket" to "${AWS_ICON_URL}Compute/Bottlerocket.puml",
        "awscompute" to "${AWS_ICON_URL}Compute/Compute.puml",
        "awscomputeoptimizer" to "${AWS_ICON_URL}Compute/ComputeOptimizer.puml",
        "awsec2" to "${AWS_ICON_URL}Compute/EC2.puml",
        "awsec2a1instance" to "${AWS_ICON_URL}Compute/EC2A1Instance.puml",
        "awsec2ami" to "${AWS_ICON_URL}Compute/EC2AMI.puml",
        "awsec2awsinferentia" to "${AWS_ICON_URL}Compute/EC2AWSInferentia.puml",
        "awsec2autoscaling" to "${AWS_ICON_URL}Compute/EC2AutoScaling.puml",
        "awsec2autoscalingresource" to "${AWS_ICON_URL}Compute/EC2AutoScalingResource.puml",
        "awsec2c4instance" to "${AWS_ICON_URL}Compute/EC2C4Instance.puml",
        "awsec2c5instance" to "${AWS_ICON_URL}Compute/EC2C5Instance.puml",
        "awsec2c5ainstance" to "${AWS_ICON_URL}Compute/EC2C5aInstance.puml",
        "awsec2c5adinstance" to "${AWS_ICON_URL}Compute/EC2C5adInstance.puml",
        "awsec2c5dinstance" to "${AWS_ICON_URL}Compute/EC2C5dInstance.puml",
        "awsec2c5ninstance" to "${AWS_ICON_URL}Compute/EC2C5nInstance.puml",
        "awsec2c6ginstance" to "${AWS_ICON_URL}Compute/EC2C6gInstance.puml",
        "awsec2c6gdinstance" to "${AWS_ICON_URL}Compute/EC2C6gdInstance.puml",
        "awsec2d2instance" to "${AWS_ICON_URL}Compute/EC2D2Instance.puml",
        "awsec2d3instance" to "${AWS_ICON_URL}Compute/EC2D3Instance.puml",
        "awsec2d3eninstance" to "${AWS_ICON_URL}Compute/EC2D3enInstance.puml",
        "awsec2dbinstance" to "${AWS_ICON_URL}Compute/EC2DBInstance.puml",
        "awsec2elasticipaddress" to "${AWS_ICON_URL}Compute/EC2ElasticIPAddress.puml",
        "awsec2f1instance" to "${AWS_ICON_URL}Compute/EC2F1Instance.puml",
        "awsec2g3instance" to "${AWS_ICON_URL}Compute/EC2G3Instance.puml",
        "awsec2g4adinstance" to "${AWS_ICON_URL}Compute/EC2G4adInstance.puml",
        "awsec2g4dninstance" to "${AWS_ICON_URL}Compute/EC2G4dnInstance.puml",
        "awsec2h1instance" to "${AWS_ICON_URL}Compute/EC2H1Instance.puml",
        "awsec2hmiinstance" to "${AWS_ICON_URL}Compute/EC2HMIInstance.puml",
        "awsec2habanagaudiinstance" to "${AWS_ICON_URL}Compute/EC2HabanaGaudiInstance.puml",
        "awsec2i2instance" to "${AWS_ICON_URL}Compute/EC2I2Instance.puml",
        "awsec2i3instance" to "${AWS_ICON_URL}Compute/EC2I3Instance.puml",
        "awsec2i3eninstance" to "${AWS_ICON_URL}Compute/EC2I3enInstance.puml",
        "awsec2imagebuilder" to "${AWS_ICON_URL}Compute/EC2ImageBuilder.puml",
        "awsec2inf1instance" to "${AWS_ICON_URL}Compute/EC2Inf1Instance.puml",
        "awsec2instance" to "${AWS_ICON_URL}Compute/EC2Instance.puml",
        "awsec2instances" to "${AWS_ICON_URL}Compute/EC2Instances.puml",
        "awsec2instancewithcloudwatch" to "${AWS_ICON_URL}Compute/EC2InstancewithCloudWatch.puml",
        "awsec2m4instance" to "${AWS_ICON_URL}Compute/EC2M4Instance.puml",
        "awsec2m5instance" to "${AWS_ICON_URL}Compute/EC2M5Instance.puml",
        "awsec2m5ainstance" to "${AWS_ICON_URL}Compute/EC2M5aInstance.puml",
        "awsec2m5dinstance" to "${AWS_ICON_URL}Compute/EC2M5dInstance.puml",
        "awsec2m5dninstance" to "${AWS_ICON_URL}Compute/EC2M5dnInstance.puml",
        "awsec2m5ninstance" to "${AWS_ICON_URL}Compute/EC2M5nInstance.puml",
        "awsec2m5zninstance" to "${AWS_ICON_URL}Compute/EC2M5znInstance.puml",
        "awsec2m6ginstance" to "${AWS_ICON_URL}Compute/EC2M6gInstance.puml",
        "awsec2m6gdinstance" to "${AWS_ICON_URL}Compute/EC2M6gdInstance.puml",
        "awsec2macinstance" to "${AWS_ICON_URL}Compute/EC2MacInstance.puml",
        "awsec2p2instance" to "${AWS_ICON_URL}Compute/EC2P2Instance.puml",
        "awsec2p3instance" to "${AWS_ICON_URL}Compute/EC2P3Instance.puml",
        "awsec2p3dninstance" to "${AWS_ICON_URL}Compute/EC2P3dnInstance.puml",
        "awsec2p4instance" to "${AWS_ICON_URL}Compute/EC2P4Instance.puml",
        "awsec2p4dinstance" to "${AWS_ICON_URL}Compute/EC2P4dInstance.puml",
        "awsec2r4instance" to "${AWS_ICON_URL}Compute/EC2R4Instance.puml",
        "awsec2r5instance" to "${AWS_ICON_URL}Compute/EC2R5Instance.puml",
        "awsec2r5ainstance" to "${AWS_ICON_URL}Compute/EC2R5aInstance.puml",
        "awsec2r5adinstance" to "${AWS_ICON_URL}Compute/EC2R5adInstance.puml",
        "awsec2r5binstance" to "${AWS_ICON_URL}Compute/EC2R5bInstance.puml",
        "awsec2r5dinstance" to "${AWS_ICON_URL}Compute/EC2R5dInstance.puml",
        "awsec2r5gdinstance" to "${AWS_ICON_URL}Compute/EC2R5gdInstance.puml",
        "awsec2r5ninstance" to "${AWS_ICON_URL}Compute/EC2R5nInstance.puml",
        "awsec2r6ginstance" to "${AWS_ICON_URL}Compute/EC2R6gInstance.puml",
        "awsec2rdninstance" to "${AWS_ICON_URL}Compute/EC2RdnInstance.puml",
        "awsec2rescue" to "${AWS_ICON_URL}Compute/EC2Rescue.puml",
        "awsec2spotinstance" to "${AWS_ICON_URL}Compute/EC2SpotInstance.puml",
        "awsec2t2instance" to "${AWS_ICON_URL}Compute/EC2T2Instance.puml",
        "awsec2t3instance" to "${AWS_ICON_URL}Compute/EC2T3Instance.puml",
        "awsec2t3ainstance" to "${AWS_ICON_URL}Compute/EC2T3aInstance.puml",
        "awsec2t4ginstance" to "${AWS_ICON_URL}Compute/EC2T4gInstance.puml",
        "awsec2trainiuminstance" to "${AWS_ICON_URL}Compute/EC2TrainiumInstance.puml",
        "awsec2x1instance" to "${AWS_ICON_URL}Compute/EC2X1Instance.puml",
        "awsec2x1einstance" to "${AWS_ICON_URL}Compute/EC2X1eInstance.puml",
        "awsec2z1dinstance" to "${AWS_ICON_URL}Compute/EC2z1dInstance.puml",
        "awselasticbeanstalk" to "${AWS_ICON_URL}Compute/ElasticBeanstalk.puml",
        "awselasticbeanstalkapplication" to "${AWS_ICON_URL}Compute/ElasticBeanstalkApplication.puml",
        "awselasticbeanstalkdeployment" to "${AWS_ICON_URL}Compute/ElasticBeanstalkDeployment.puml",
        "awselasticfabricadapter" to "${AWS_ICON_URL}Compute/ElasticFabricAdapter.puml",
        "awsfargate2" to "${AWS_ICON_URL}Compute/Fargate2.puml",
        "awslambda" to "${AWS_ICON_URL}Compute/Lambda.puml",
        "awslambdalambdafunction" to "${AWS_ICON_URL}Compute/LambdaLambdaFunction.puml",
        "awslightsail" to "${AWS_ICON_URL}Compute/Lightsail.puml",
        "awslocalzones" to "${AWS_ICON_URL}Compute/LocalZones.puml",
        "awsnicedcv" to "${AWS_ICON_URL}Compute/NICEDCV.puml",
        "awsnitroenclaves" to "${AWS_ICON_URL}Compute/NitroEnclaves.puml",
        "awsoutposts" to "${AWS_ICON_URL}Compute/Outposts.puml",
        "awsoutposts1uand2uservers" to "${AWS_ICON_URL}Compute/Outposts1Uand2UServers.puml",
        "awsparallelcluster" to "${AWS_ICON_URL}Compute/ParallelCluster.puml",
        "awsserverlessapplicationrepository" to "${AWS_ICON_URL}Compute/ServerlessApplicationRepository.puml",
        "awsthinkboxdeadline" to "${AWS_ICON_URL}Compute/ThinkBoxDeadline.puml",
        "awsthinkboxfrost" to "${AWS_ICON_URL}Compute/ThinkBoxFrost.puml",
        "awsthinkboxkrakatoa" to "${AWS_ICON_URL}Compute/ThinkBoxKrakatoa.puml",
        "awsthinkboxsequoia" to "${AWS_ICON_URL}Compute/ThinkBoxSequoia.puml",
        "awsthinkboxstoke" to "${AWS_ICON_URL}Compute/ThinkBoxStoke.puml",
        "awsthinkboxxmesh" to "${AWS_ICON_URL}Compute/ThinkBoxXMesh.puml",
        "awsvmwarecloudonaws" to "${AWS_ICON_URL}Compute/VMwareCloudonAWS.puml",
        "awswavelength" to "${AWS_ICON_URL}Compute/Wavelength.puml",
        "awscontainers" to "${AWS_ICON_URL}Containers/Containers.puml",
        "awseksanywhere" to "${AWS_ICON_URL}Containers/EKSAnywhere.puml",
        "awsekscloud" to "${AWS_ICON_URL}Containers/EKSCloud.puml",
        "awseksdistro" to "${AWS_ICON_URL}Containers/EKSDistro.puml",
        "awselasticcontainerregistry" to "${AWS_ICON_URL}Containers/ElasticContainerRegistry.puml",
        "awselasticcontainerregistryimage" to "${AWS_ICON_URL}Containers/ElasticContainerRegistryImage.puml",
        "awselasticcontainerregistryregistry" to "${AWS_ICON_URL}Containers/ElasticContainerRegistryRegistry.puml",
        "awselasticcontainerservice" to "${AWS_ICON_URL}Containers/ElasticContainerService.puml",
        "awselasticcontainerservicecontainer1" to "${AWS_ICON_URL}Containers/ElasticContainerServiceContainer1.puml",
        "awselasticcontainerservicecontainer2" to "${AWS_ICON_URL}Containers/ElasticContainerServiceContainer2.puml",
        "awselasticcontainerservicecontainer3" to "${AWS_ICON_URL}Containers/ElasticContainerServiceContainer3.puml",
        "awselasticcontainerservicecopilotcli" to "${AWS_ICON_URL}Containers/ElasticContainerServiceCopilotCLI.puml",
        "awselasticcontainerserviceecsanywhere" to "${AWS_ICON_URL}Containers/ElasticContainerServiceECSAnywhere.puml",
        "awselasticcontainerserviceservice" to "${AWS_ICON_URL}Containers/ElasticContainerServiceService.puml",
        "awselasticcontainerservicetask" to "${AWS_ICON_URL}Containers/ElasticContainerServiceTask.puml",
        "awselastickubernetesservice" to "${AWS_ICON_URL}Containers/ElasticKubernetesService.puml",
        "awsfargate" to "${AWS_ICON_URL}Containers/Fargate.puml",
        "awsredhatopenshift" to "${AWS_ICON_URL}Containers/RedHatOpenShift.puml",
        "awsaurora" to "${AWS_ICON_URL}Database/Aurora.puml",
        "awsauroraamazonaurorainstancealternate" to "${AWS_ICON_URL}Database/AuroraAmazonAuroraInstancealternate.puml",
        "awsauroraamazonrdsinstance" to "${AWS_ICON_URL}Database/AuroraAmazonRDSInstance.puml",
        "awsauroraamazonrdsinstanceaternate" to "${AWS_ICON_URL}Database/AuroraAmazonRDSInstanceAternate.puml",
        "awsaurorainstance" to "${AWS_ICON_URL}Database/AuroraInstance.puml",
        "awsauroramariadbinstance" to "${AWS_ICON_URL}Database/AuroraMariaDBInstance.puml",
        "awsauroramariadbinstancealternate" to "${AWS_ICON_URL}Database/AuroraMariaDBInstanceAlternate.puml",
        "awsauroramysqlinstance" to "${AWS_ICON_URL}Database/AuroraMySQLInstance.puml",
        "awsauroramysqlinstancealternate" to "${AWS_ICON_URL}Database/AuroraMySQLInstanceAlternate.puml",
        "awsauroraoracleinstance" to "${AWS_ICON_URL}Database/AuroraOracleInstance.puml",
        "awsauroraoracleinstancealternate" to "${AWS_ICON_URL}Database/AuroraOracleInstanceAlternate.puml",
        "awsaurorapiopsinstance" to "${AWS_ICON_URL}Database/AuroraPIOPSInstance.puml",
        "awsaurorapostgresqlinstance" to "${AWS_ICON_URL}Database/AuroraPostgreSQLInstance.puml",
        "awsaurorapostgresqlinstancealternate" to "${AWS_ICON_URL}Database/AuroraPostgreSQLInstanceAlternate.puml",
        "awsaurorasqlserverinstance" to "${AWS_ICON_URL}Database/AuroraSQLServerInstance.puml",
        "awsaurorasqlserverinstance" to "${AWS_ICON_URL}Database/AuroraSQLServerInstance.puml",
        "awsaurorasqlserverinstancealternate" to "${AWS_ICON_URL}Database/AuroraSQLServerInstanceAlternate.puml",
        "awsdatabase" to "${AWS_ICON_URL}Database/Database.puml",
        "awsdatabasemigrationservice" to "${AWS_ICON_URL}Database/DatabaseMigrationService.puml",
        "awsdocumentdb" to "${AWS_ICON_URL}Database/DocumentDB.puml",
        "awsdynamodb" to "${AWS_ICON_URL}Database/DynamoDB.puml",
        "awsdynamodbamazondynamodbaccelerator" to "${AWS_ICON_URL}Database/DynamoDBAmazonDynamoDBAccelerator.puml",
        "awsdynamodbattribute" to "${AWS_ICON_URL}Database/DynamoDBAttribute.puml",
        "awsdynamodbattributes" to "${AWS_ICON_URL}Database/DynamoDBAttributes.puml",
        "awsdynamodbglobalsecondaryindex" to "${AWS_ICON_URL}Database/DynamoDBGlobalsecondaryindex.puml",
        "awsdynamodbitem" to "${AWS_ICON_URL}Database/DynamoDBItem.puml",
        "awsdynamodbitems" to "${AWS_ICON_URL}Database/DynamoDBItems.puml",
        "awsdynamodbstream" to "${AWS_ICON_URL}Database/DynamoDBStream.puml",
        "awsdynamodbtable" to "${AWS_ICON_URL}Database/DynamoDBTable.puml",
        "awselasticache" to "${AWS_ICON_URL}Database/ElastiCache.puml",
        "awselasticachecachenode" to "${AWS_ICON_URL}Database/ElastiCacheCacheNode.puml",
        "awselasticacheelasticacheformemcached" to "${AWS_ICON_URL}Database/ElastiCacheElastiCacheforMemcached.puml",
        "awselasticacheelasticacheforredis" to "${AWS_ICON_URL}Database/ElastiCacheElastiCacheforRedis.puml",
        "awskeyspaces" to "${AWS_ICON_URL}Database/Keyspaces.puml",
        "awsneptune" to "${AWS_ICON_URL}Database/Neptune.puml",
        "awsquantumledgerdatabase2" to "${AWS_ICON_URL}Database/QuantumLedgerDatabase2.puml",
        "awsrds" to "${AWS_ICON_URL}Database/RDS.puml",
        "awsrdsproxyinstance" to "${AWS_ICON_URL}Database/RDSProxyInstance.puml",
        "awsrdsproxyinstancealternate" to "${AWS_ICON_URL}Database/RDSProxyInstanceAlternate.puml",
        "awsrdsonvmware" to "${AWS_ICON_URL}Database/RDSonVMware.puml",
        "awstimestream" to "${AWS_ICON_URL}Database/Timestream.puml",
        "awsamplify" to "${AWS_ICON_URL}FrontEndWebMobile/Amplify.puml",
        "awsdevicefarm" to "${AWS_ICON_URL}FrontEndWebMobile/DeviceFarm.puml",
        "awsfrontendwebmobile" to "${AWS_ICON_URL}FrontEndWebMobile/FrontEndWebMobile.puml",
        "awslocationservice" to "${AWS_ICON_URL}FrontEndWebMobile/LocationService.puml",
        "awslocationservicegeofence" to "${AWS_ICON_URL}FrontEndWebMobile/LocationServiceGeofence.puml",
        "awslocationservicemap" to "${AWS_ICON_URL}FrontEndWebMobile/LocationServiceMap.puml",
        "awslocationserviceplace" to "${AWS_ICON_URL}FrontEndWebMobile/LocationServicePlace.puml",
        "awslocationserviceroutes" to "${AWS_ICON_URL}FrontEndWebMobile/LocationServiceRoutes.puml",
        "awslocationservicetrack" to "${AWS_ICON_URL}FrontEndWebMobile/LocationServiceTrack.puml",
        "awsautoscalinggroup" to "${AWS_ICON_URL}GroupIcons/AutoScalingGroup.puml",
        "awsnew" to "${AWS_ICON_URL}GroupIcons/Cloud.puml",
        "aws" to "${AWS_ICON_URL}GroupIcons/Cloudalt.puml",
        "awscorporatedatacenter" to "${AWS_ICON_URL}GroupIcons/CorporateDataCenter.puml",
        "awsec2instancecontainer" to "${AWS_ICON_URL}GroupIcons/EC2InstanceContainer.puml",
        "awselasticbeanstalkcontainer" to "${AWS_ICON_URL}GroupIcons/ElasticBeanstalkContainer.puml",
        "awsregion" to "${AWS_ICON_URL}GroupIcons/Region.puml",
        "awsservercontents" to "${AWS_ICON_URL}GroupIcons/ServerContents.puml",
        "awsspotfleet" to "${AWS_ICON_URL}GroupIcons/SpotFleet.puml",
        "awsstepfunction" to "${AWS_ICON_URL}GroupIcons/StepFunction.puml",
        "awsvpcsubnetprivate" to "${AWS_ICON_URL}GroupIcons/VPCSubnetPrivate.puml",
        "awsvpcsubnetpublic" to "${AWS_ICON_URL}GroupIcons/VPCSubnetPublic.puml",
        "awsvirtualprivatecloudvpc" to "${AWS_ICON_URL}GroupIcons/VirtualPrivateCloudVPC.puml",
        "awsappmesh" to "${AWS_ICON_URL}NetworkingContentDelivery/AppMesh.puml",
        "awsappmeshmesh" to "${AWS_ICON_URL}NetworkingContentDelivery/AppMeshMesh.puml",
        "awsappmeshvirtualgateway" to "${AWS_ICON_URL}NetworkingContentDelivery/AppMeshVirtualGateway.puml",
        "awsappmeshvirtualnode" to "${AWS_ICON_URL}NetworkingContentDelivery/AppMeshVirtualNode.puml",
        "awsappmeshvirtualrouter" to "${AWS_ICON_URL}NetworkingContentDelivery/AppMeshVirtualRouter.puml",
        "awsappmeshvirtualservice" to "${AWS_ICON_URL}NetworkingContentDelivery/AppMeshVirtualService.puml",
        "awsclientvpn" to "${AWS_ICON_URL}NetworkingContentDelivery/ClientVPN.puml",
        "awsclouddirectory2" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudDirectory2.puml",
        "awscloudfront" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudFront.puml",
        "awscloudfrontdownloaddistribution" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudFrontDownloadDistribution.puml",
        "awscloudfrontedgelocation" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudFrontEdgeLocation.puml",
        "awscloudfrontfunctions" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudFrontFunctions.puml",
        "awscloudfrontstreamingdistribution" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudFrontStreamingDistribution.puml",
        "awscloudmap" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudMap.puml",
        "awscloudmapnamespace" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudMapNamespace.puml",
        "awscloudmapresource" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudMapResource.puml",
        "awscloudmapservice" to "${AWS_ICON_URL}NetworkingContentDelivery/CloudMapService.puml",
        "awsdirectconnect" to "${AWS_ICON_URL}NetworkingContentDelivery/DirectConnect.puml",
        "awsdirectconnectgateway" to "${AWS_ICON_URL}NetworkingContentDelivery/DirectConnectGateway.puml",
        "awselasticloadbalancing" to "${AWS_ICON_URL}NetworkingContentDelivery/ElasticLoadBalancing.puml",
        "awsapplicationloadbalancer" to "${AWS_ICON_URL}NetworkingContentDelivery/ElasticLoadBalancingApplicationLoadBalancer.puml",
        "awsclassicloadbalancer" to "${AWS_ICON_URL}NetworkingContentDelivery/ElasticLoadBalancingClassicLoadBalancer.puml",
        "awsgatewayloadbalancer" to "${AWS_ICON_URL}NetworkingContentDelivery/ElasticLoadBalancingGatewayLoadBalancer.puml",
        "awsnetworkloadbalancer" to "${AWS_ICON_URL}NetworkingContentDelivery/ElasticLoadBalancingNetworkLoadBalancer.puml",
        "awsglobalaccelerator" to "${AWS_ICON_URL}NetworkingContentDelivery/GlobalAccelerator.puml",
        "awsnetworkingcontentdelivery" to "${AWS_ICON_URL}NetworkingContentDelivery/NetworkingContentDelivery.puml",
        "awsprivatelink" to "${AWS_ICON_URL}NetworkingContentDelivery/PrivateLink.puml",
        "awsroute53" to "${AWS_ICON_URL}NetworkingContentDelivery/Route53.puml",
        "awsroute53hostedzone" to "${AWS_ICON_URL}NetworkingContentDelivery/Route53HostedZone.puml",
        "awsroute53resolver" to "${AWS_ICON_URL}NetworkingContentDelivery/Route53Resolver.puml",
        "awsroute53resolverdnsfirewall" to "${AWS_ICON_URL}NetworkingContentDelivery/Route53ResolverDNSFirewall.puml",
        "awsroute53resolverquerylogging" to "${AWS_ICON_URL}NetworkingContentDelivery/Route53ResolverQueryLogging.puml",
        "awsroute53routetable" to "${AWS_ICON_URL}NetworkingContentDelivery/Route53RouteTable.puml",
        "awssitetositevpn" to "${AWS_ICON_URL}NetworkingContentDelivery/SitetoSiteVPN.puml",
        "awstransitgateway" to "${AWS_ICON_URL}NetworkingContentDelivery/TransitGateway.puml",
        "awsvpccarriergateway" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCCarrierGateway.puml",
        "awsvpccustomergateway" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCCustomerGateway.puml",
        "awsvpcelasticnetworkadapter" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCElasticNetworkAdapter.puml",
        "awsvpcelasticnetworkinterface" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCElasticNetworkInterface.puml",
        "awsvpcendpoints" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCEndpoints.puml",
        "awsvpcflowlogs" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCFlowLogs.puml",
        "awsvpcinternetgateway" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCInternetGateway.puml",
        "awsvpcnatgateway" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCNATGateway.puml",
        "awsvpcnetworkaccesscontrollist" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCNetworkAccessControlList.puml",
        "awsvpcpeeringconnection" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCPeeringConnection.puml",
        "awsvpcreachabilityanalyzer" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCReachabilityAnalyzer.puml",
        "awsvpcrouter" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCRouter.puml",
        "awsvpctrafficmirroring" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCTrafficMirroring.puml",
        "awsvpcvpnconnection" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCVPNConnection.puml",
        "awsvpcvpngateway" to "${AWS_ICON_URL}NetworkingContentDelivery/VPCVPNGateway.puml",
        "awsvirtualprivatecloud" to "${AWS_ICON_URL}NetworkingContentDelivery/VirtualPrivateCloud.puml",
        "awsbackup" to "${AWS_ICON_URL}Storage/Backup.puml",
        "awsbackupbackupplan" to "${AWS_ICON_URL}Storage/BackupBackupPlan.puml",
        "awsbackupbackuprestore" to "${AWS_ICON_URL}Storage/BackupBackupRestore.puml",
        "awsbackupbackupvault" to "${AWS_ICON_URL}Storage/BackupBackupVault.puml",
        "awsbackupcompliancereporting" to "${AWS_ICON_URL}Storage/BackupComplianceReporting.puml",
        "awsbackuprecoverypointobjective" to "${AWS_ICON_URL}Storage/BackupRecoveryPointObjective.puml",
        "awsbackuprecoverytimeobjective" to "${AWS_ICON_URL}Storage/BackupRecoveryTimeObjective.puml",
        "awscloudenduredisasterrecovery" to "${AWS_ICON_URL}Storage/CloudEndureDisasterRecovery.puml",
        "awselasticblockstore" to "${AWS_ICON_URL}Storage/ElasticBlockStore.puml",
        "awselasticblockstoreamazondatalifecyclemanager" to "${AWS_ICON_URL}Storage/ElasticBlockStoreAmazonDataLifecycleManager.puml",
        "awselasticblockstoremultiplevolumes" to "${AWS_ICON_URL}Storage/ElasticBlockStoreMultipleVolumes.puml",
        "awselasticblockstoresnapshot" to "${AWS_ICON_URL}Storage/ElasticBlockStoreSnapshot.puml",
        "awselasticblockstorevolume" to "${AWS_ICON_URL}Storage/ElasticBlockStoreVolume.puml",
        "awselasticblockstorevolumegp3" to "${AWS_ICON_URL}Storage/ElasticBlockStoreVolumegp3.puml",
        "awselasticfilesystem" to "${AWS_ICON_URL}Storage/ElasticFileSystem.puml",
        "awselasticfilesystemfilesystem" to "${AWS_ICON_URL}Storage/ElasticFileSystemFileSystem.puml",
        "awselasticfilesystemonezone" to "${AWS_ICON_URL}Storage/ElasticFileSystemOneZone.puml",
        "awselasticfilesystemonezoneinfrequentaccess" to "${AWS_ICON_URL}Storage/ElasticFileSystemOneZoneInfrequentAccess.puml",
        "awselasticfilesystemstandard" to "${AWS_ICON_URL}Storage/ElasticFileSystemStandard.puml",
        "awselasticfilesystemstandardinfrequentaccess" to "${AWS_ICON_URL}Storage/ElasticFileSystemStandardInfrequentAccess.puml",
        "awsfsx" to "${AWS_ICON_URL}Storage/FSx.puml",
        "awsfsxforlustre" to "${AWS_ICON_URL}Storage/FSxforLustre.puml",
        "awsfsxforwindowsfileserver" to "${AWS_ICON_URL}Storage/FSxforWindowsFileServer.puml",
        "awss3onoutpostsstorage" to "${AWS_ICON_URL}Storage/S3OnOutpostsStorage.puml",
        "awss3" to "${AWS_ICON_URL}Storage/SimpleStorageService.puml",
        "awss3bucket" to "${AWS_ICON_URL}Storage/SimpleStorageServiceBucket.puml",
        "awss3bucketwithobjects" to "${AWS_ICON_URL}Storage/SimpleStorageServiceBucketWithObjects.puml",
        "awss3generalaccesspoints" to "${AWS_ICON_URL}Storage/SimpleStorageServiceGeneralAccessPoints.puml",
        "awss3glacier" to "${AWS_ICON_URL}Storage/SimpleStorageServiceGlacier.puml",
        "awss3glacierarchive" to "${AWS_ICON_URL}Storage/SimpleStorageServiceGlacierArchive.puml",
        "awss3glaciervault" to "${AWS_ICON_URL}Storage/SimpleStorageServiceGlacierVault.puml",
        "awss3object" to "${AWS_ICON_URL}Storage/SimpleStorageServiceObject.puml",
        "awss3glacier" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3Glacier.puml",
        "awss3glacierdeeparchive" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3GlacierDeepArchive.puml",
        "awss3intelligenttiering" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3IntelligentTiering.puml",
        "awss3objectlambda" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3ObjectLambda.puml",
        "awss3objectlambdaaccesspoints" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3ObjectLambdaAccessPoints.puml",
        "awss3onoutposts" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3OnOutposts.puml",
        "awss3onezoneia" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3OneZoneIA.puml",
        "awss3replication" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3Replication.puml",
        "awss3replicationtimecontrol" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3ReplicationTimeControl.puml",
        "awss3standard" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3Standard.puml",
        "awss3standardia" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3StandardIA.puml",
        "awss3storagelens" to "${AWS_ICON_URL}Storage/SimpleStorageServiceS3StorageLens.puml",
        "awss3vpcaccesspoints" to "${AWS_ICON_URL}Storage/SimpleStorageServiceVPCAccessPoints.puml",
        "awssnowball" to "${AWS_ICON_URL}Storage/Snowball.puml",
        "awssnowballedge" to "${AWS_ICON_URL}Storage/SnowballEdge.puml",
        "awssnowballsnowballimportexport" to "${AWS_ICON_URL}Storage/SnowballSnowballImportExport.puml",
        "awssnowcone" to "${AWS_ICON_URL}Storage/Snowcone.puml",
        "awssnowmobile" to "${AWS_ICON_URL}Storage/Snowmobile.puml",
        "awsstorage" to "${AWS_ICON_URL}Storage/Storage.puml",
        "awsstoragegateway" to "${AWS_ICON_URL}Storage/StorageGateway.puml",
        "awsstoragegatewayamazonfsxfilegateway" to "${AWS_ICON_URL}Storage/StorageGatewayAmazonFSxFileGateway.puml",
        "awsstoragegatewayamazons3filegateway" to "${AWS_ICON_URL}Storage/StorageGatewayAmazonS3FileGateway.puml",
        "awsstoragegatewaycachedvolume" to "${AWS_ICON_URL}Storage/StorageGatewayCachedVolume.puml",
        "awsstoragegatewayfilegateway" to "${AWS_ICON_URL}Storage/StorageGatewayFileGateway.puml",
        "awsstoragegatewaynoncachedvolume" to "${AWS_ICON_URL}Storage/StorageGatewayNonCachedVolume.puml",
        "awsstoragegatewaytapegateway" to "${AWS_ICON_URL}Storage/StorageGatewayTapeGateway.puml",
        "awsstoragegatewayvirtualtapelibrary" to "${AWS_ICON_URL}Storage/StorageGatewayVirtualTapeLibrary.puml",
        "awsstoragegatewayvolumegateway" to "${AWS_ICON_URL}Storage/StorageGatewayVolumeGateway.puml",
    )

    init {
        reset()
    }
}
