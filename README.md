This reproduces https://github.com/open-telemetry/opentelemetry-java-instrumentation/issues/11851 on:
```
❯ java --version
openjdk 21.0.3 2024-04-16
OpenJDK Runtime Environment (Red_Hat-21.0.3.0.9-1) (build 21.0.3+9)
OpenJDK 64-Bit Server VM (Red_Hat-21.0.3.0.9-1) (build 21.0.3+9, mixed mode, sharing)

❯ grep PRETTY /etc/os-release
PRETTY_NAME="Fedora Linux 40 (KDE Plasma)"
```

# Run

Execute `./bin/start ${ENDPOINT} ${USERNAME} ${PASSWORD}` in a terminal, where you replace `${ENDPOINT}` with the URL to
your ClickHouse instance, e.g. https://example.clickhouse.cloud:8443, and `${USERNAME}` and `${PASSWORD}` with the
username and password for that instance.

On success, you should see an output like the following:
```
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
[otel.javaagent 2024-07-18 12:13:48:089 +0200] [main] INFO io.opentelemetry.javaagent.tooling.VersionLogger - opentelemetry-javaagent - version: 2.6.0
```

## Missing command line arguments

If you fail to provide the command line args, you will see an output like the following:
```
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
[otel.javaagent 2024-07-18 12:20:34:902 +0200] [main] INFO io.opentelemetry.javaagent.tooling.VersionLogger - opentelemetry-javaagent - version: 2.6.0
Execution error (IllegalArgumentException) at com.clickhouse.data.ClickHouseChecker/newException (ClickHouseChecker.java:17).
Endpoints cannot be null or empty string

Full report at:
/tmp/clojure-4874223971135042206.edn
```

In this case make sure you provide 3 arguments to `./bin/start`, as described above.

## Missing OpenTelemetry stack

If exporting the OpenTelemetry traces fails, you will see an output like the following:
```
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
[otel.javaagent 2024-07-18 12:19:22:151 +0200] [main] INFO io.opentelemetry.javaagent.tooling.VersionLogger - opentelemetry-javaagent - version: 2.6.0
[otel.javaagent 2024-07-18 12:19:27:048 +0200] [OkHttp http://localhost:4317/...] WARN io.opentelemetry.exporter.internal.grpc.GrpcExporter - Failed to export metrics. Server responded with gRPC status code 2. Error message: Failed to connect to localhost/[0:0:0:0:0:0:0:1]:4317
[otel.javaagent 2024-07-18 12:19:27:048 +0200] [OkHttp http://localhost:4317/...] WARN io.opentelemetry.exporter.internal.grpc.GrpcExporter - Failed to export spans. Server responded with gRPC status code 2. Error message: Failed to connect to localhost/[0:0:0:0:0:0:0:1]:4317
```

If you do not have an OpenTelemetry stack set up, you can execute `./bin/aspire-dashboard` to start one locally.
Navigate to http://localhost:18888/traces to view the traces it collects.
