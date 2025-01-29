FROM ubuntu:24.04

LABEL INPUTS="GIT_SSH GIT_KEY"
LABEL NAME="Git Resource"
LABEL DESCRIPTION="Fetches new revisions, checks them out and starts a pipeline"
LABEL STORAGE="TRUE"

RUN apt-get update && apt-get install -y git

WORKDIR /step

COPY checkout.sh .

CMD ["bash", "/step/checkout.sh"]
