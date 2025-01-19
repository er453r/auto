# Automation

Just a webhook test.

### Projects

`Projects` are just a set of user-defined `ENV`s that are periodically run against available steps.

### Pipeline

`Pipeline` is a collection of steps with `ENV`s and `workdir` docker `volume` mutated along the way.

### Steps

`Step` is started when all `ENV`s it defines as inputs are available in the pipeline.

- features
  - nodes started on available `ENV` values in the pipeline
  - `STATELESS` by desigin, if `STORAGE` label is present - have `STATE` (kept by the implementation in the permanent `volume`)
  - need to define input ENVs in docker image `LABEL`s
- inputs
  - permanent storage `storage`
  - workdir `volume`
  - `ENV` (from previous node)
- outputs
  - workdir `volume` (modified)
  - `ENV` (modified, but printing ENV=VALUE to `stdout`)
