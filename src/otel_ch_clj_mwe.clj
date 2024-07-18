(ns otel-ch-clj-mwe
 (:import
  (com.clickhouse.client ClickHouseClient ClickHouseNodes ClickHouseProtocol ClickHouseRequest$Mutation)
  (com.clickhouse.client.config ClickHouseClientOption ClickHouseDefaults)
  (com.clickhouse.client.http.config ClickHouseHttpOption HttpConnectionProvider)
  (com.clickhouse.config ClickHouseOption)
  (com.clickhouse.data ClickHouseFormat)
  (java.net URI)))

; Comments look like this
; Print values using `(println "value" a 1)`.

(defn- make-nodes
 ^ClickHouseNodes [endpoint username password]
 (ClickHouseNodes/of endpoint (-> {ClickHouseDefaults/USER                   username
                                   ClickHouseDefaults/PASSWORD               password
                                   ClickHouseHttpOption/CONNECTION_PROVIDER  HttpConnectionProvider/HTTP_CLIENT
                                   ClickHouseClientOption/CONNECTION_TIMEOUT (* 1 60 1000)
                                   ClickHouseClientOption/SOCKET_TIMEOUT     (* 5 60 1000)}
                                  (update-keys #(.getKey ^ClickHouseOption %)))))

(defn- make-client
  ^ClickHouseClient [endpoint]
  (let [protocol (ClickHouseProtocol/fromUriScheme (.getScheme (URI. endpoint)))]
    (ClickHouseClient/newInstance (into-array ClickHouseProtocol [protocol]))))

(defn- make-request
  ^ClickHouseRequest$Mutation [^ClickHouseClient client ^ClickHouseNodes nodes ^String query]
 (doto (.write (.read client nodes))
   (.format ClickHouseFormat/RowBinaryWithNamesAndTypes)
   (.query query)))

(defn- execute!
  [^ClickHouseClient client nodes query]
  (let [request (make-request client nodes query)]
    (.close (.executeAndWait client request))))

(defn -main
 [& [endpoint username password]]
 (let [nodes (make-nodes endpoint username password)]
  (with-open [client (make-client endpoint)]
   (execute! client nodes "SELECT 1=1"))))
