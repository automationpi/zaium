FROM node:20-bookworm-slim

RUN --mount=type=cache,target=/var/cache/apt,sharing=locked \
    --mount=type=cache,target=/var/lib/apt,sharing=locked \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        python3 python3-pip g++ build-essential \
        gcc musl-dev curl graphviz \
        fonts-dejavu fonts-liberation fontconfig

# Setup for TechDocs to render Mermaid
RUN echo "deb http://deb.debian.org/debian bullseye main contrib non-free" >> /etc/apt/sources.list && \
    echo "deb-src http://deb.debian.org/debian bullseye main contrib non-free" >> /etc/apt/sources.list && \
    apt-get update

# Install openJDK 11
RUN curl --insecure -fsSLO https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.9.1%2B1/OpenJDK11U-jdk_x64_linux_hotspot_11.0.9.1_1.tar.gz && \
    tar -xvf OpenJDK11U-jdk_x64_linux_hotspot_11.0.9.1_1.tar.gz && \
    mv jdk-11.0.9.1+1 /usr/local/openjdk11

ENV PATH=$PATH:/usr/local/openjdk11/bin
ENV JAVA_HOME=/usr/local/openjdk11

# RUN pip3 install mkdocs-techdocs-core==1.1.7



RUN npm install -g node-gyp
RUN npm install -g @techdocs/cli

RUN find /usr/lib -name "EXTERNALLY-MANAGED" -exec rm -rf {} \;

RUN python3 -m pip install mkdocs-techdocs-core
RUN python3 -m pip install mkdocs-kroki-plugin==0.8.0
RUN python3 -m pip install cookiecutter
RUN python3 -m pip install mkdocs-awesome-pages-plugin

# Switch to a non-root user
USER node

# Set the working directory
WORKDIR /app

# Set environment to production
ENV NODE_ENV production

# Set the default command to run when a container is started
CMD ["node"]

