FROM ubuntu:24.04

LABEL INPUTS="GIT_SSH GIT_KEY"
LABEL NAME="Git Resource"
LABEL DESCRIPTION="Fetches new revisions, checks them out and start a pipeline"

WORKDIR /step

COPY checkout.sh .

CMD ["bash", "/step/checkout.sh"]
