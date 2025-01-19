FROM ubuntu:24.04

LABEL INPUTS="GIT_SSH GIT_KEY"
LABEL NAME="Git Resource"
LABEL DESCRIPTION="Fetches new revisions, checks them out and starts a pipeline"

WORKDIR /step

COPY run.sh .

CMD ["bash", "/step/run.sh"]
