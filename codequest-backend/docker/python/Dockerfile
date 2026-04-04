FROM python:3.11-slim

RUN useradd -m -s /bin/bash runner

WORKDIR /sandbox

RUN chmod 755 /sandbox

USER runner

CMD ["python3", "solution.py"]
