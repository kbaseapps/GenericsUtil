FROM kbase/kbase:sdkbase.latest
MAINTAINER KBase Developer
# -----------------------------------------

# Insert apt-get instructions here to install
# any required dependencies for your module.

# RUN apt-get update

# update jars to latest, to get opencsv

RUN . /kb/dev_container/user-env.sh && \
  cd /kb/dev_container/modules && \
  rm -rf jars && \
  git clone https://github.com/kbase/jars && \
  cd /kb/dev_container/modules/jars && make && make deploy

WORKDIR /kb/module
RUN mkdir -p /kb/module/dependencies/bin

WORKDIR /kb/module/dependencies/bin
RUN curl -L https://raw.githubusercontent.com/kbase/transform/develop/plugins/scripts/download/ont.pl -o ont.pl && \
    chmod 777 ont.pl

# -----------------------------------------

COPY ./ /kb/module
RUN mkdir -p /kb/module/work
RUN chmod 777 /kb/module

WORKDIR /kb/module

RUN make all

ENTRYPOINT [ "./scripts/entrypoint.sh" ]

CMD [ ]
