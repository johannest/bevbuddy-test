# HOWTO:
# mvn clean install -Pproduction -DskipTests
# cp ../target/bevbuddy-1.0-SNAPSHOT.war bevbuddy-app.war
# scp -i aws-keypair-eu.pem * ec2-user@ipaddress:.
# ssh -i aws-keypair-eu.pem ec2-user@ipaddress
# docker build . --rm -t bevbuddy-server && docker run -it --rm --ulimit nofile=90000:90000 -p 8080:8080 bevbuddy-server
FROM anapsix/alpine-java:8_server-jre
ADD bevbuddy-app.war .

ENTRYPOINT java -jar \
                -Xmx50g -Xms50g \
                -Dvaadin.productionMode \
                bevbuddy-app.war