# Use the same Maven + JDK 21 image as in Jenkins
FROM maven:3.9.9-eclipse-temurin-21

# Set up environment variables (matching Jenkinsfile)
ENV MAVEN_OPTS="\
    --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED \
    --add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED \
    --add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
    --add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED \
    --add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED \
    --add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
    --add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED \
    --add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
    --add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED \
    -Djava.awt.headless=true"

# For headless GUI testing (if needed)
ENV DISPLAY=:99

# Create a directory for the project
WORKDIR /app

# Copy the Maven POM first (for layer caching)
COPY testng.xml .
COPY pom.xml .

# Download dependencies (cached unless POM changes)
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Default command (matches Jenkins' build step)
CMD ["mvn", "clean", "test"]