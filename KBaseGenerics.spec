module KBaseGenerics{

    typedef string data_type;
    typedef string oterm_ref;
    typedef string object_ref;

    /*
    @optional object_ref oterm_ref int_value float_value string_value
    */
    typedef structure{
        data_type   scalar_type;

        object_ref  object_ref;
        oterm_ref   oterm_ref;
        int         int_value;
        float       float_value;
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
        list<string>      string_values;
    } Values;

    /*
    @optional object_refs oterm_refs int_values float_values string_values
    */
    typedef structure{
        data_type   scalar_type;

        list<list<object_ref>>  object_refs;
        list<list<oterm_ref>>   oterm_refs;
        list<list<int>>         int_values;
        list<list<float>>       float_values;
        list<list<string>>      string_values;
    } Values2D;

    /*
    @optional object_refs oterm_refs int_values float_values string_values
    */
    typedef structure{
        data_type   scalar_type;

        list<list<list<object_ref>>>  object_refs;
        list<list<list<oterm_ref>>>   oterm_refs;
        list<list<list<int>>>         int_values;
        list<list<list<float>>>       float_values;
        list<list<list<string>>>      string_values;
    } Values3D;

    /*
    @optional oterm_ref oterm_name
    */
    typedef structure{
        string term_name;
        oterm_ref oterm_ref;
        string oterm_name;
    } Term;


    /*
    @optional units
    */
    typedef structure{
        Term property;
        Term units;
        Value value;
    } ContextItem;


    /*
    @optional units
    */
    typedef structure{
        Term property;
        Term units;
        Values values;
    } DimensionContextItem;

    typedef structure{
        int dimension_size;
        list<DimensionContextItem> items;
    } DimensionContext;

    typedef structure{
        string name;
        string description;
        Term data_type;

        list<ContextItem> array_context;
        DimensionContext x_context;

        Term    value_type;
        Term    value_units;         
        Values  values;        

    } Array;

    typedef structure{
        string name;
        string description;
        Term data_type;

        list<ContextItem> matrix_context;

        DimensionContext x_context; 
        DimensionContext y_context; 

        Term        value_type;
        Term        value_units;         
        Values2D    values;        
    } Matrix2D;


    typedef structure{
        string name;
        string description;
        Term data_type;

        list<ContextItem> matrix_context;

        DimensionContext x_context; 
        DimensionContext y_context; 
        DimensionContext z_context;

        Term        value_type;
        Term        value_units;         
        Values3D    values;        
    } Matrix3D;


    typedef structure{
        string name;
        string description;
        Term data_type;

        list<ContextItem> array_context;
        int dimension_number;
        list<DimensionContext> dimensions_context;

        Term        value_type;
        Term        value_units;         
        Values      values;        
    } NDArray;



    typedef structure{
        string name;
        string description;
        Term data_type;

        list<ContextItem> matrix_context;

        DimensionContext x_context; 
        DimensionContext y_context; 
        int replicates_number;

        Term        value_type;
        Term        value_units;         
        list<list<list<float>>>  values;        
    } Matrix2DFloatWithReplicates;


    typedef structure{
        string name;
        string description;
        Term data_type;

        list<ContextItem> matrix_context;

        DimensionContext x_context; 
        DimensionContext y_context; 

        Term        value_type;
        Term        value_units;

        list<list<float>>    avg_values;        
        list<list<float>>    std_values;        
        list<list<float>>    se_values;        
    } Matrix2DFloatWithStat;

};