module KBaseGenerics{

    typedef int boolean;
    typedef string data_type;
    typedef string oterm_ref;
    typedef string object_ref;

    /*
    scalar_type can be one of:
        object_ref
        oterm_ref
        int
        float
        boolean
        string

    @optional object_ref oterm_ref int_value float_value string_value
    */
    typedef structure{
        data_type   scalar_type;

        object_ref  object_ref;
        oterm_ref   oterm_ref;
        int         int_value;
        float       float_value;
        boolean     boolean_value;
        string      string_value;
    } Value;

    /*
    @optional object_refs oterm_refs int_values float_values string_values
    */
    typedef structure{
        data_type   scalar_type;

        list<object_ref>  object_refs;
        list<oterm_ref>   oterm_refs;
        list<int>         int_values;
        list<float>       float_values;
        list<boolean>     boolean_values;
        list<string>      string_values;
    } Values;

    /*
    @optional oterm_ref oterm_name
    */
    typedef structure{
        string term_name;
        oterm_ref oterm_ref;
        string oterm_name;
    } Term;

    /*
    @optional value_units
    */
    typedef structure{
        Term value_type;
        Term value_units;
        Value value;
    } TypedValue;


    /*
    @optional value_units value_context
    */
    typedef structure{
        Term value_type;
        Term value_units;
        list<TypedValue> value_context;
        Values values;
    } TypedValues;


    typedef structure{
        Term data_type;
        int size;
        list<TypedValues> typed_values;
    } DimensionContext;

    /*
    @optional array_context
    */
    typedef structure{
        string name;
        string description;
        Term data_type;
        list<TypedValue> array_context;

        int n_dimensions;
        list<DimensionContext> dim_context;
        TypedValues typed_values;
    } NDArray;

    /*
    @optional array_context
    */
    typedef structure{
        string name;
        string description;
        Term data_type;
        list<TypedValue> array_context;

        int n_dimensions;
        list<DimensionContext> dim_context;
        list<TypedValues> typed_values;
    } HNDArray;

    /*
    //-----------------
    // Types used for mapping
    //-----------------
    */

    typedef structure{
        string ontology_id;
        string oterm_id;
        string oterm_name;

        string value_scalar_type;
        string value_mapper;
        
        string value_validator_method;
        string value_validation_pattern;

        float rank_score;
    } OTerm;

    typedef structure{
        string term_name;
        list<OTerm> oterms;
    } M_Term;

    typedef structure{
        M_Term value_type;
        M_Term value_units;
        M_Term value;        
    } M_TypedValue;

    typedef structure{
        M_Term value_type;
        M_Term value_units;
        list<TypedValue> value_context;        
        list<M_Term> values;
    } M_TypedValues;    

    typedef structure{
        M_Term data_type;
        int size;
        list<M_TypedValues> typed_values;
    } M_DimensionContext;

    typedef structure{
        string name;
        string description;
        M_Term data_type;
        list<M_TypedValue> array_context;
        

        int n_dimensions;
        list<M_DimensionContext> dim_context;
        M_TypedValues typed_values;
    } M_NDArray;

};

