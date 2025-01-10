# Automation

Just a webhook test.

## Nodes

### Resources

- nodes started periodically
- they initiate a pipeline
- have `STATE` (kept by the implementation in the permanent `volume`)
- inputs
  - permanent `volume`
  - `ENV` (user-defined)
- outputs
  - `ENV` (modified)

### Steps

- nodes started on available ENV values in the pipeline
- `STATELESS`
- inputs
  - workdir `volume`
  - `ENV` (from previous node)
- outputs
  - workdir `volume` (modified)
  - `ENV` (modified)
