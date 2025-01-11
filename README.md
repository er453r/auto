# Automation

Just a webhook test.

## Nodes

### Resources

- features
  - nodes started periodically
  - they initiate a pipeline
  - have `STATE` (kept by the implementation in the permanent `volume`)
  - need to define input ENVs in docker image `LABEL`s
- inputs
  - permanent storage `volume`
  - temporary `volume` for potential pipeline start
  - `ENV` (user-defined)
- outputs
  - `ENV` (modified, but printing ENV=VALUE to `stdout`)

### Steps

- features
  - nodes started on available ENV values in the pipeline
  - `STATELESS`
  - need to define input ENVs in docker image `LABEL`s
- inputs
  - workdir `volume`
  - `ENV` (from previous node)
- outputs
  - workdir `volume` (modified)
  - `ENV` (modified, but printing ENV=VALUE to `stdout`)
