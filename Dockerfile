# Stage 1: Build the application
FROM openjdk:17-jdk-slim-bullseye AS builder

# Set environment variables
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$PATH:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

# Install necessary packages
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    git \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Download and install Android SDK
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip -O /tmp/tools.zip && \
    unzip -q /tmp/tools.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm /tmp/tools.zip

# Accept licenses
RUN yes | sdkmanager --licenses

# Install required Android SDK components
RUN sdkmanager "platform-tools" "platforms;android-35" "build-tools;34.0.0"

# Set working directory
WORKDIR /app

# Copy the entire project
COPY . .

# Make gradlew executable
RUN if [ -f "./gradlew" ]; then chmod +x ./gradlew; fi

# Build the application
RUN if [ -f "./gradlew" ]; then \
    ./gradlew assembleRelease; \
    elif [ -f "./gradlew.bat" ]; then \
    chmod +x ./gradlew.bat && \
    ./gradlew.bat assembleRelease; \
    else \
    echo "No Gradle wrapper found. Please run 'gradle wrapper' in your project."; \
    exit 1; \
    fi

# Stage 2: Create minimal runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the APK or JAR from the builder stage
# For APK:
COPY --from=builder /app/app/build/outputs/apk/release/*.apk ./
# For JAR (uncomment if you have a JAR instead):
# COPY --from=builder /app/app/build/libs/*.jar ./

# Optional: Set entry point if you want to run the JAR
# ENTRYPOINT ["java", "-jar", "your-app.jar"]

# Command to simply hold the container active
CMD ["sh", "-c", "echo 'Container started' && tail -f /dev/null"]