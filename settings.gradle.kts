rootProject.name = "microservices-demo"
include("stream-to-kafka-service")
include("common-config")
include("app-config-data")
include("kafka")
include("kafka:kafka-model")
findProject(":kafka:kafka-model")?.name = "kafka-model"
include("kafka:kafka-admin")
findProject(":kafka:kafka-admin")?.name = "kafka-admin"
include("kafka:kafka-producer")
findProject(":kafka:kafka-producer")?.name = "kafka-producer"




pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
