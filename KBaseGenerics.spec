module KBaseGenerics {
    typedef int bool;

    /*
    Generic reference to another KBase WS object or subobject.
    Note that we need to devise a good format for subobject
    referencing since this is not really handled by WS service.
    @id ws;
    */
    typedef string ref;

    /*
    Specific reference to an ontology term; see growth.obo or
    units.obo for ontologies we have uploaded into KBase already.  

    @id subws KBaseOntologies.OntologyDictionary.term_hash.[key]
    */
    typedef string ontology_term_ref;

    /*
    Note that "Object" means a ref to WS objects or subobjects (features,
								genomes, etc); we could consider making objects and subobjects 2 distinct things.
    Ontology means a reference to an ontological term (specifically, to a
						       key in the "term_hash" variable in a KBaseOntologies.OntologyDictionary).
    enum: int, boolean, float, string, object; ontology;
    */
    typedef string data_type;

    /*
    This stores a single piece of metadata.  Depending on the setting
    of "primitive type", the data will be stored in one of the
    following places:
    int - int_data
    boolean - int_data (as a 0 or 1)
    float - float_data
    string - string_data
    object - object_ref_data (NOTE: subobject refs may need to be stored as 2 strings?
			      We may also want to store the type of object that the ref maps to, which
			      is currently stored in the ontology; e.g., KBaseGenomeAnnotations.Taxon)
    ontology - ontology_ref_data (NOTE: could also store in same string as object)
    */
    typedef structure {
        data_type primitive_type;
        ref object_ref_data;
        ontology_term_ref ontology_ref_data;
        int int_data;
        float float_data;
        string string_data;
    } DataValue;

    /*
    This stores values for either data or metadata, along with
    metadata describing it.  This is just like DataValue, but with an array
    of data.  n is the length of the list being used.  Other lists are null.
    The list being used may include some null values.
    */
    typedef structure {
        data_type primitive_type;
        int n;
        list<ref> object_ref_data;
        list<ontology_term_ref> ontology_ref_data;
        list<int> int_data;
        list<float> float_data;
        list<string> string_data;
    } DataValues;
    
    /*
    A single piece of metadata (i.e., metadatum)
    */
    typedef structure{
        string original_description; 
        ontology_term_ref measurement; 
        ontology_term_ref units; 
        list<ontology_term_ref> context; 
    } MetadataItem;

    /*
    One piece of metadata describing the entire matrix.
    */
    typedef structure {
        MetadataItem metadata;
        DataValue value;
    } MatrixMetadataItem;

    /*
    One piece of metadata describing a dimension of a matrix; e.g., row or column.  The DataValues
    object must be the same length as the dimension, even if some of the actual values are null.
    */
    typedef structure {
        MetadataItem metadata;
        DataValues values;
    } DimensionMetadataItem;

    /*
    A multi-dimensional generic data matrix.  When matrix is first uploaded, ontologies
    will not yet be mapped, so ontologies_mapped will be 0 and all text descriptions will be
    stored in the "original_description" string of MetadataItem objects.  After mapping
    all ontological terms and references to other KBase objects, ontologies_mapped will be
    set to 1, and we will allow users to use the object (e.g., display graphs).

    n_dimensions - number of dimensions of data
    dimension_length - the length of each dimension
    data are stored in the "data" array, in row major order
    values_metadata describes what the data are (e.g., units, what is being measured)
    matrix_metadata contains other optional properties describing the whole matrix (e.g.,
										    other metadata describing the experiment)
    dimension_metadata stores a list of properties of each dimension.  Each dimension may
    have multiple types of metadata; e.g., nitrate concentration and formate concentration.

    @optional name description matrix_metadata
    */
    typedef structure {
        string name;
        string description;
        bool ontologies_mapped;  
        int n_dimensions;
        list<int> dimension_length;
        DataValues data;
        MatrixMetadataItem values_metadata;
        list<MatrixMetadataItem> matrix_metadata;
        list<list<DimensionMetadataItem>> dimension_metadata;
    } DataMatrix;
};

