# ArtsAPI ETL

The ETL tooling for ArtsAPI, based on the [Grafter project](http://grafter.org). Mainly focussed on email parsing and RDF conversion. This is intended to generate data and seed the DB for running an ArtsAPI instance. You can find the code and a Docker image for deploying one [here](https://github.com/Swirrl/artsapi).

## Ontology

In the `doc` folder, there is a version controlled turtle representation of the ArtsAPI ontology, as well as a number of example resources.

## UK SIC 2007 Ontology

In the `doc` folder, there is an ontology and RDF resources that correspond to the UK SIC 2007 framework. The data was created using the `sic2007` template in the project, from the source Excel file in `doc`, sourced from the [official ONS page](http://www.ons.gov.uk/ons/guide-method/classifications/current-standard-classifications/standard-industrial-classification/index.html).

### SIC Extensions

In order to classify specific activities within the arts sector that have not been included within the SIC hierarchy, it has been extended with some *un-official* sub-classes, taken directly from Arts Council England's categories for different artforms. These are listed below, and you may contextualise them by browsing the [interactive hierarchy](http://www.neighbourhood.statistics.gov.uk/HTMLDocs/SIC/ONS_SIC_hierarchy_view.html) available on the ONS website.

Subclasses, with new unofficial SIC classifications:

1. Combined Arts [R 90.03/1]
2. Dance [R 90.01/1]
3. Literature [R 90.03/2]
4. Music [R 90.01/2]
5. Theatre [R 90.01/3]
6. Visual Arts [R 90.03/3]

These can be found as linked data resources in `doc/sic_extensions.ttl`.

It is worth noting as well that these might for example be considered to also belong (in purely economic terms) to other categories - for example, you _might_ prefer to consider 'Literature' as being more correctly suited to being a subclass of `Section J, Division 58, Group 58.1, Class 58.11: Book Publishing`. Another example might be that 'Music' could also be under `Section J, Division 59, Group 59.2, Class 59.20: Sound recording and music publishing activities` - 'Music' is therefore, for the purposes of this heuristic, considered a performing art, rather than a recorded work. In any case, these subtle ambiguities are left to the user to unpick.

In order to add the Charitable sector, an extra heading has been added - Section V, which breaks down into one subdivision and two groups, `100.1` and `100.2`. These contain two Classes for use, detailed here:

1. Charitable activities [V 100.10]
2. Voluntary or unpaid activities n.e.c. [V 100.20]

These can be found as linked data resources in `doc/sic_extensions.ttl`.

NB: In SIC, 'n.e.c.' is used throughout to mean 'Not Elsewhere Classified'.

## Usage

Use `lein run <path-to-mbox-file> <destination.ttl>` to convert Email or Twitter data. The tool expects you to pass it an absolute path to an mbox file, or the JavaScript `tweet` folder of a Twitter dump. From the root of a Twitter dump, this directory may be found at `./data/js/tweets`.

To import into a db, use `lein run <path-to-mbox-file> <query-endpoint> <update-endpoint>` and your emails will be converted and loaded into the db in batches.

*Please Note*: very large mbox files have to be loaded into memory first. This is why the JVM memory allocation is set very high (7GB) - reduce this in `project.clj` if necessary. This converter has been tested with this configuration with files up to 2.85GB and will still run correctly. Any larger and you will have to break your mbox file up into smaller collections before you can convert it.

## License

Copyright © 2014-2015 Swirrl IT Limited

Distributed under the Eclipse Public License 1.0
