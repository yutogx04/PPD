FROM node:20-slim

RUN useradd -m -s /bin/bash runner

WORKDIR /sandbox

RUN chmod 755 /sandbox

USER runner

CMD ["node", "solution.js"]
