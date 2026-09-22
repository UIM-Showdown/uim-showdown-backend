# UIM Showdown

This repository consists of two components:
- A Spring Boot application that handles player contributions and event point calculations found in the `/server` subdirectory.
- A Vue 3 application that will replace the Google Sheet technology and display competition information found in the `/client` subdirectory.

**NOTE:** Each subdirectory has an `README.md` file with more technical detail.

## Development Principles
These principles have been outlined in our tech explainer video found here: https://www.youtube.com/watch?v=x48Y3whtbcA

1. Keep it simple/standard
    - To account for the various people working on the tech for this event with varying levels of experience
    - To avoid the situation where only a singular person can work on a given feature due to obscurity or complexity.
2. Extensible, but reasonable
    - Not everything needs to configurable, there are matters (such as certain rates) that are considered to be constant.
    - Extensibility should be considered when it is reasonable, and not only when it would be a nice to have.
3. No AI usage
    - There are a multitude of reasons on how the team came to this decision. Please refer to the tech explainer video above for specifics.
