# ArtsAPI ETL

The ETL tooling for ArtsAPI, based on the [Grafter project](http://grafter.org). Mainly focussed on email parsing and RDF conversion. 

## Ontology

In the `doc` folder, there is a version controlled turtle representation of the ArtsAPI ontology, as well as a number of example resources.

## UK SIC 2007 Ontology

In the `doc` folder, there is an ontology and RDF resources that correspond to the UK SIC 2007 framework. The data was created using the `sic2007` template in the project, from the source Excel file in `doc`, sourced from the [official ONS page](http://www.ons.gov.uk/ons/guide-method/classifications/current-standard-classifications/standard-industrial-classification/index.html). 

## Usage

Use `lein run <path-to-emails-or-twitter> <destination.ttl>` to convert Email or Twitter data. The tool expects you to pass it an absolute path to an mbox file, or the JavaScript `tweet` folder of a Twitter dump. From the root of a Twitter dump, this directory may be found at `./data/js/tweets`.

TODO

## License

Copyright © 2014 Swirrl IT Limited

Distributed under the Eclipse Public License 1.0
