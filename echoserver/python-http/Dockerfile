FROM python:2

WORKDIR /usr/src/app

# COPY requirements.txt ./
# RUN pip install --no-cache-dir -r requirements.txt

# RUN yum install -y 

COPY web-server.py .

CMD [ "python", "-u", "./web-server.py" ]
