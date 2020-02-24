Spike is broken but the concept is:

- onBindView adds view to a class that stores the view and a timestamp.
- Every x amount of time perform a visibility check on views to see if they are 50% or more on screen.
- If view has been on screen for x seconds send an impression stat.
