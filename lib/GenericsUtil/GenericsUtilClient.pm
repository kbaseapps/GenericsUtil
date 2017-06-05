package GenericsUtil::GenericsUtilClient;

use JSON::RPC::Client;
use POSIX;
use strict;
use Data::Dumper;
use URI;
use Bio::KBase::Exceptions;
my $get_time = sub { time, 0 };
eval {
    require Time::HiRes;
    $get_time = sub { Time::HiRes::gettimeofday() };
};

use Bio::KBase::AuthToken;

# Client version should match Impl version
# This is a Semantic Version number,
# http://semver.org
our $VERSION = "0.1.0";

=head1 NAME

GenericsUtil::GenericsUtilClient

=head1 DESCRIPTION


A KBase module: GenericsUtil.  Utilities for manipulating
Generic objects.


=cut

sub new
{
    my($class, $url, @args) = @_;
    

    my $self = {
	client => GenericsUtil::GenericsUtilClient::RpcClient->new,
	url => $url,
	headers => [],
    };

    chomp($self->{hostname} = `hostname`);
    $self->{hostname} ||= 'unknown-host';

    #
    # Set up for propagating KBRPC_TAG and KBRPC_METADATA environment variables through
    # to invoked services. If these values are not set, we create a new tag
    # and a metadata field with basic information about the invoking script.
    #
    if ($ENV{KBRPC_TAG})
    {
	$self->{kbrpc_tag} = $ENV{KBRPC_TAG};
    }
    else
    {
	my ($t, $us) = &$get_time();
	$us = sprintf("%06d", $us);
	my $ts = strftime("%Y-%m-%dT%H:%M:%S.${us}Z", gmtime $t);
	$self->{kbrpc_tag} = "C:$0:$self->{hostname}:$$:$ts";
    }
    push(@{$self->{headers}}, 'Kbrpc-Tag', $self->{kbrpc_tag});

    if ($ENV{KBRPC_METADATA})
    {
	$self->{kbrpc_metadata} = $ENV{KBRPC_METADATA};
	push(@{$self->{headers}}, 'Kbrpc-Metadata', $self->{kbrpc_metadata});
    }

    if ($ENV{KBRPC_ERROR_DEST})
    {
	$self->{kbrpc_error_dest} = $ENV{KBRPC_ERROR_DEST};
	push(@{$self->{headers}}, 'Kbrpc-Errordest', $self->{kbrpc_error_dest});
    }

    #
    # This module requires authentication.
    #
    # We create an auth token, passing through the arguments that we were (hopefully) given.

    {
	my %arg_hash2 = @args;
	if (exists $arg_hash2{"token"}) {
	    $self->{token} = $arg_hash2{"token"};
	} elsif (exists $arg_hash2{"user_id"}) {
	    my $token = Bio::KBase::AuthToken->new(@args);
	    if (!$token->error_message) {
	        $self->{token} = $token->token;
	    }
	}
	
	if (exists $self->{token})
	{
	    $self->{client}->{token} = $self->{token};
	}
    }

    my $ua = $self->{client}->ua;	 
    my $timeout = $ENV{CDMI_TIMEOUT} || (30 * 60);	 
    $ua->timeout($timeout);
    bless $self, $class;
    #    $self->_validate_version();
    return $self;
}




=head2 import_csv

  $result = $obj->import_csv($params)

=over 4

=item Parameter and return types

=begin html

<pre>
$params is a GenericsUtil.ImportCSVParams
$result is a GenericsUtil.ImportResult
ImportCSVParams is a reference to a hash where the following keys are defined:
	file has a value which is a GenericsUtil.File
	workspace_name has a value which is a string
	object_name has a value which is a string
	object_type has a value which is a string
	metadata has a value which is a GenericsUtil.usermeta
File is a reference to a hash where the following keys are defined:
	path has a value which is a string
	shock_id has a value which is a string
usermeta is a reference to a hash where the key is a string and the value is a string
ImportResult is a reference to a hash where the following keys are defined:
	object_ref has a value which is a string

</pre>

=end html

=begin text

$params is a GenericsUtil.ImportCSVParams
$result is a GenericsUtil.ImportResult
ImportCSVParams is a reference to a hash where the following keys are defined:
	file has a value which is a GenericsUtil.File
	workspace_name has a value which is a string
	object_name has a value which is a string
	object_type has a value which is a string
	metadata has a value which is a GenericsUtil.usermeta
File is a reference to a hash where the following keys are defined:
	path has a value which is a string
	shock_id has a value which is a string
usermeta is a reference to a hash where the key is a string and the value is a string
ImportResult is a reference to a hash where the following keys are defined:
	object_ref has a value which is a string


=end text

=item Description



=back

=cut

 sub import_csv
{
    my($self, @args) = @_;

# Authentication: required

    if ((my $n = @args) != 1)
    {
	Bio::KBase::Exceptions::ArgumentValidationError->throw(error =>
							       "Invalid argument count for function import_csv (received $n, expecting 1)");
    }
    {
	my($params) = @args;

	my @_bad_arguments;
        (ref($params) eq 'HASH') or push(@_bad_arguments, "Invalid type for argument 1 \"params\" (value was \"$params\")");
        if (@_bad_arguments) {
	    my $msg = "Invalid arguments passed to import_csv:\n" . join("", map { "\t$_\n" } @_bad_arguments);
	    Bio::KBase::Exceptions::ArgumentValidationError->throw(error => $msg,
								   method_name => 'import_csv');
	}
    }

    my $url = $self->{url};
    my $result = $self->{client}->call($url, $self->{headers}, {
	    method => "GenericsUtil.import_csv",
	    params => \@args,
    });
    if ($result) {
	if ($result->is_error) {
	    Bio::KBase::Exceptions::JSONRPC->throw(error => $result->error_message,
					       code => $result->content->{error}->{code},
					       method_name => 'import_csv',
					       data => $result->content->{error}->{error} # JSON::RPC::ReturnObject only supports JSONRPC 1.1 or 1.O
					      );
	} else {
	    return wantarray ? @{$result->result} : $result->result->[0];
	}
    } else {
        Bio::KBase::Exceptions::HTTP->throw(error => "Error invoking method import_csv",
					    status_line => $self->{client}->status_line,
					    method_name => 'import_csv',
				       );
    }
}
 


=head2 import_obo

  $result = $obj->import_obo($params)

=over 4

=item Parameter and return types

=begin html

<pre>
$params is a GenericsUtil.ImportOBOParams
$result is a GenericsUtil.ImportResult
ImportOBOParams is a reference to a hash where the following keys are defined:
	file has a value which is a GenericsUtil.File
	workspace_name has a value which is a string
	object_name has a value which is a string
	metadata has a value which is a GenericsUtil.usermeta
File is a reference to a hash where the following keys are defined:
	path has a value which is a string
	shock_id has a value which is a string
usermeta is a reference to a hash where the key is a string and the value is a string
ImportResult is a reference to a hash where the following keys are defined:
	object_ref has a value which is a string

</pre>

=end html

=begin text

$params is a GenericsUtil.ImportOBOParams
$result is a GenericsUtil.ImportResult
ImportOBOParams is a reference to a hash where the following keys are defined:
	file has a value which is a GenericsUtil.File
	workspace_name has a value which is a string
	object_name has a value which is a string
	metadata has a value which is a GenericsUtil.usermeta
File is a reference to a hash where the following keys are defined:
	path has a value which is a string
	shock_id has a value which is a string
usermeta is a reference to a hash where the key is a string and the value is a string
ImportResult is a reference to a hash where the following keys are defined:
	object_ref has a value which is a string


=end text

=item Description



=back

=cut

 sub import_obo
{
    my($self, @args) = @_;

# Authentication: required

    if ((my $n = @args) != 1)
    {
	Bio::KBase::Exceptions::ArgumentValidationError->throw(error =>
							       "Invalid argument count for function import_obo (received $n, expecting 1)");
    }
    {
	my($params) = @args;

	my @_bad_arguments;
        (ref($params) eq 'HASH') or push(@_bad_arguments, "Invalid type for argument 1 \"params\" (value was \"$params\")");
        if (@_bad_arguments) {
	    my $msg = "Invalid arguments passed to import_obo:\n" . join("", map { "\t$_\n" } @_bad_arguments);
	    Bio::KBase::Exceptions::ArgumentValidationError->throw(error => $msg,
								   method_name => 'import_obo');
	}
    }

    my $url = $self->{url};
    my $result = $self->{client}->call($url, $self->{headers}, {
	    method => "GenericsUtil.import_obo",
	    params => \@args,
    });
    if ($result) {
	if ($result->is_error) {
	    Bio::KBase::Exceptions::JSONRPC->throw(error => $result->error_message,
					       code => $result->content->{error}->{code},
					       method_name => 'import_obo',
					       data => $result->content->{error}->{error} # JSON::RPC::ReturnObject only supports JSONRPC 1.1 or 1.O
					      );
	} else {
	    return wantarray ? @{$result->result} : $result->result->[0];
	}
    } else {
        Bio::KBase::Exceptions::HTTP->throw(error => "Error invoking method import_obo",
					    status_line => $self->{client}->status_line,
					    method_name => 'import_obo',
				       );
    }
}
 


=head2 export_csv

  $result = $obj->export_csv($params)

=over 4

=item Parameter and return types

=begin html

<pre>
$params is a GenericsUtil.ExportParams
$result is a GenericsUtil.ExportResult
ExportParams is a reference to a hash where the following keys are defined:
	input_ref has a value which is a string
ExportResult is a reference to a hash where the following keys are defined:
	shock_id has a value which is a string

</pre>

=end html

=begin text

$params is a GenericsUtil.ExportParams
$result is a GenericsUtil.ExportResult
ExportParams is a reference to a hash where the following keys are defined:
	input_ref has a value which is a string
ExportResult is a reference to a hash where the following keys are defined:
	shock_id has a value which is a string


=end text

=item Description



=back

=cut

 sub export_csv
{
    my($self, @args) = @_;

# Authentication: required

    if ((my $n = @args) != 1)
    {
	Bio::KBase::Exceptions::ArgumentValidationError->throw(error =>
							       "Invalid argument count for function export_csv (received $n, expecting 1)");
    }
    {
	my($params) = @args;

	my @_bad_arguments;
        (ref($params) eq 'HASH') or push(@_bad_arguments, "Invalid type for argument 1 \"params\" (value was \"$params\")");
        if (@_bad_arguments) {
	    my $msg = "Invalid arguments passed to export_csv:\n" . join("", map { "\t$_\n" } @_bad_arguments);
	    Bio::KBase::Exceptions::ArgumentValidationError->throw(error => $msg,
								   method_name => 'export_csv');
	}
    }

    my $url = $self->{url};
    my $result = $self->{client}->call($url, $self->{headers}, {
	    method => "GenericsUtil.export_csv",
	    params => \@args,
    });
    if ($result) {
	if ($result->is_error) {
	    Bio::KBase::Exceptions::JSONRPC->throw(error => $result->error_message,
					       code => $result->content->{error}->{code},
					       method_name => 'export_csv',
					       data => $result->content->{error}->{error} # JSON::RPC::ReturnObject only supports JSONRPC 1.1 or 1.O
					      );
	} else {
	    return wantarray ? @{$result->result} : $result->result->[0];
	}
    } else {
        Bio::KBase::Exceptions::HTTP->throw(error => "Error invoking method export_csv",
					    status_line => $self->{client}->status_line,
					    method_name => 'export_csv',
				       );
    }
}
 


=head2 list_generic_objects

  $result = $obj->list_generic_objects($params)

=over 4

=item Parameter and return types

=begin html

<pre>
$params is a GenericsUtil.ListGenericObjectsParams
$result is a GenericsUtil.ListGenericObjectsResult
ListGenericObjectsParams is a reference to a hash where the following keys are defined:
	workspace_names has a value which is a reference to a list where each element is a string
	allowed_object_types has a value which is a reference to a list where each element is a string
	allowed_data_types has a value which is a reference to a list where each element is a string
	allowed_scalar_types has a value which is a reference to a list where each element is a string
	min_dimensions has a value which is an int
	max_dimensions has a value which is an int
	limit_mapped has a value which is an int
ListGenericObjectsResult is a reference to a hash where the following keys are defined:
	object_ids has a value which is a reference to a list where each element is a string

</pre>

=end html

=begin text

$params is a GenericsUtil.ListGenericObjectsParams
$result is a GenericsUtil.ListGenericObjectsResult
ListGenericObjectsParams is a reference to a hash where the following keys are defined:
	workspace_names has a value which is a reference to a list where each element is a string
	allowed_object_types has a value which is a reference to a list where each element is a string
	allowed_data_types has a value which is a reference to a list where each element is a string
	allowed_scalar_types has a value which is a reference to a list where each element is a string
	min_dimensions has a value which is an int
	max_dimensions has a value which is an int
	limit_mapped has a value which is an int
ListGenericObjectsResult is a reference to a hash where the following keys are defined:
	object_ids has a value which is a reference to a list where each element is a string


=end text

=item Description



=back

=cut

 sub list_generic_objects
{
    my($self, @args) = @_;

# Authentication: required

    if ((my $n = @args) != 1)
    {
	Bio::KBase::Exceptions::ArgumentValidationError->throw(error =>
							       "Invalid argument count for function list_generic_objects (received $n, expecting 1)");
    }
    {
	my($params) = @args;

	my @_bad_arguments;
        (ref($params) eq 'HASH') or push(@_bad_arguments, "Invalid type for argument 1 \"params\" (value was \"$params\")");
        if (@_bad_arguments) {
	    my $msg = "Invalid arguments passed to list_generic_objects:\n" . join("", map { "\t$_\n" } @_bad_arguments);
	    Bio::KBase::Exceptions::ArgumentValidationError->throw(error => $msg,
								   method_name => 'list_generic_objects');
	}
    }

    my $url = $self->{url};
    my $result = $self->{client}->call($url, $self->{headers}, {
	    method => "GenericsUtil.list_generic_objects",
	    params => \@args,
    });
    if ($result) {
	if ($result->is_error) {
	    Bio::KBase::Exceptions::JSONRPC->throw(error => $result->error_message,
					       code => $result->content->{error}->{code},
					       method_name => 'list_generic_objects',
					       data => $result->content->{error}->{error} # JSON::RPC::ReturnObject only supports JSONRPC 1.1 or 1.O
					      );
	} else {
	    return wantarray ? @{$result->result} : $result->result->[0];
	}
    } else {
        Bio::KBase::Exceptions::HTTP->throw(error => "Error invoking method list_generic_objects",
					    status_line => $self->{client}->status_line,
					    method_name => 'list_generic_objects',
				       );
    }
}
 


=head2 get_generic_metadata

  $result = $obj->get_generic_metadata($params)

=over 4

=item Parameter and return types

=begin html

<pre>
$params is a GenericsUtil.GetGenericMetadataParams
$result is a GenericsUtil.GetGenericMetadataResult
GetGenericMetadataParams is a reference to a hash where the following keys are defined:
	object_ids has a value which is a reference to a list where each element is a string
GetGenericMetadataResult is a reference to a hash where the following keys are defined:
	object_info has a value which is a reference to a hash where the key is a string and the value is a GenericsUtil.GenericMetadata
GenericMetadata is a reference to a hash where the following keys are defined:
	object_type has a value which is a string
	data_type has a value which is a string
	n_dimensions has a value which is an int
	is_mapped has a value which is a GenericsUtil.boolean
	value_types has a value which is a reference to a list where each element is a string
	scalar_types has a value which is a reference to a list where each element is a string
	dimension_types has a value which is a reference to a list where each element is a string
	dimension_sizes has a value which is a reference to a list where each element is an int
	has_subindices has a value which is a reference to a list where each element is a GenericsUtil.boolean
	dimension_value_types has a value which is a reference to a list where each element is a reference to a list where each element is a string
	dimension_scalar_types has a value which is a reference to a list where each element is a reference to a list where each element is a string
boolean is an int

</pre>

=end html

=begin text

$params is a GenericsUtil.GetGenericMetadataParams
$result is a GenericsUtil.GetGenericMetadataResult
GetGenericMetadataParams is a reference to a hash where the following keys are defined:
	object_ids has a value which is a reference to a list where each element is a string
GetGenericMetadataResult is a reference to a hash where the following keys are defined:
	object_info has a value which is a reference to a hash where the key is a string and the value is a GenericsUtil.GenericMetadata
GenericMetadata is a reference to a hash where the following keys are defined:
	object_type has a value which is a string
	data_type has a value which is a string
	n_dimensions has a value which is an int
	is_mapped has a value which is a GenericsUtil.boolean
	value_types has a value which is a reference to a list where each element is a string
	scalar_types has a value which is a reference to a list where each element is a string
	dimension_types has a value which is a reference to a list where each element is a string
	dimension_sizes has a value which is a reference to a list where each element is an int
	has_subindices has a value which is a reference to a list where each element is a GenericsUtil.boolean
	dimension_value_types has a value which is a reference to a list where each element is a reference to a list where each element is a string
	dimension_scalar_types has a value which is a reference to a list where each element is a reference to a list where each element is a string
boolean is an int


=end text

=item Description



=back

=cut

 sub get_generic_metadata
{
    my($self, @args) = @_;

# Authentication: required

    if ((my $n = @args) != 1)
    {
	Bio::KBase::Exceptions::ArgumentValidationError->throw(error =>
							       "Invalid argument count for function get_generic_metadata (received $n, expecting 1)");
    }
    {
	my($params) = @args;

	my @_bad_arguments;
        (ref($params) eq 'HASH') or push(@_bad_arguments, "Invalid type for argument 1 \"params\" (value was \"$params\")");
        if (@_bad_arguments) {
	    my $msg = "Invalid arguments passed to get_generic_metadata:\n" . join("", map { "\t$_\n" } @_bad_arguments);
	    Bio::KBase::Exceptions::ArgumentValidationError->throw(error => $msg,
								   method_name => 'get_generic_metadata');
	}
    }

    my $url = $self->{url};
    my $result = $self->{client}->call($url, $self->{headers}, {
	    method => "GenericsUtil.get_generic_metadata",
	    params => \@args,
    });
    if ($result) {
	if ($result->is_error) {
	    Bio::KBase::Exceptions::JSONRPC->throw(error => $result->error_message,
					       code => $result->content->{error}->{code},
					       method_name => 'get_generic_metadata',
					       data => $result->content->{error}->{error} # JSON::RPC::ReturnObject only supports JSONRPC 1.1 or 1.O
					      );
	} else {
	    return wantarray ? @{$result->result} : $result->result->[0];
	}
    } else {
        Bio::KBase::Exceptions::HTTP->throw(error => "Error invoking method get_generic_metadata",
					    status_line => $self->{client}->status_line,
					    method_name => 'get_generic_metadata',
				       );
    }
}
 


=head2 get_generic_dimension_labels

  $result = $obj->get_generic_dimension_labels($params)

=over 4

=item Parameter and return types

=begin html

<pre>
$params is a GenericsUtil.GetGenericDimensionLabelsParams
$result is a GenericsUtil.GetGenericDimensionLabelsResult
GetGenericDimensionLabelsParams is a reference to a hash where the following keys are defined:
	object_id has a value which is a string
	dimension_ids has a value which is a reference to a list where each element is a string
	convert_to_string has a value which is a GenericsUtil.boolean
	unique_values has a value which is a GenericsUtil.boolean
boolean is an int
GetGenericDimensionLabelsResult is a reference to a hash where the following keys are defined:
	dimension_labels has a value which is a reference to a hash where the key is a string and the value is a KBaseGenerics.Values
Values is a reference to a hash where the following keys are defined:
	scalar_type has a value which is a KBaseGenerics.data_type
	object_refs has a value which is a reference to a list where each element is a KBaseGenerics.object_ref
	oterm_refs has a value which is a reference to a list where each element is a KBaseGenerics.oterm_ref
	int_values has a value which is a reference to a list where each element is an int
	float_values has a value which is a reference to a list where each element is a float
	boolean_values has a value which is a reference to a list where each element is a KBaseGenerics.boolean
	string_values has a value which is a reference to a list where each element is a string
data_type is a string
object_ref is a string
oterm_ref is a string

</pre>

=end html

=begin text

$params is a GenericsUtil.GetGenericDimensionLabelsParams
$result is a GenericsUtil.GetGenericDimensionLabelsResult
GetGenericDimensionLabelsParams is a reference to a hash where the following keys are defined:
	object_id has a value which is a string
	dimension_ids has a value which is a reference to a list where each element is a string
	convert_to_string has a value which is a GenericsUtil.boolean
	unique_values has a value which is a GenericsUtil.boolean
boolean is an int
GetGenericDimensionLabelsResult is a reference to a hash where the following keys are defined:
	dimension_labels has a value which is a reference to a hash where the key is a string and the value is a KBaseGenerics.Values
Values is a reference to a hash where the following keys are defined:
	scalar_type has a value which is a KBaseGenerics.data_type
	object_refs has a value which is a reference to a list where each element is a KBaseGenerics.object_ref
	oterm_refs has a value which is a reference to a list where each element is a KBaseGenerics.oterm_ref
	int_values has a value which is a reference to a list where each element is an int
	float_values has a value which is a reference to a list where each element is a float
	boolean_values has a value which is a reference to a list where each element is a KBaseGenerics.boolean
	string_values has a value which is a reference to a list where each element is a string
data_type is a string
object_ref is a string
oterm_ref is a string


=end text

=item Description



=back

=cut

 sub get_generic_dimension_labels
{
    my($self, @args) = @_;

# Authentication: required

    if ((my $n = @args) != 1)
    {
	Bio::KBase::Exceptions::ArgumentValidationError->throw(error =>
							       "Invalid argument count for function get_generic_dimension_labels (received $n, expecting 1)");
    }
    {
	my($params) = @args;

	my @_bad_arguments;
        (ref($params) eq 'HASH') or push(@_bad_arguments, "Invalid type for argument 1 \"params\" (value was \"$params\")");
        if (@_bad_arguments) {
	    my $msg = "Invalid arguments passed to get_generic_dimension_labels:\n" . join("", map { "\t$_\n" } @_bad_arguments);
	    Bio::KBase::Exceptions::ArgumentValidationError->throw(error => $msg,
								   method_name => 'get_generic_dimension_labels');
	}
    }

    my $url = $self->{url};
    my $result = $self->{client}->call($url, $self->{headers}, {
	    method => "GenericsUtil.get_generic_dimension_labels",
	    params => \@args,
    });
    if ($result) {
	if ($result->is_error) {
	    Bio::KBase::Exceptions::JSONRPC->throw(error => $result->error_message,
					       code => $result->content->{error}->{code},
					       method_name => 'get_generic_dimension_labels',
					       data => $result->content->{error}->{error} # JSON::RPC::ReturnObject only supports JSONRPC 1.1 or 1.O
					      );
	} else {
	    return wantarray ? @{$result->result} : $result->result->[0];
	}
    } else {
        Bio::KBase::Exceptions::HTTP->throw(error => "Error invoking method get_generic_dimension_labels",
					    status_line => $self->{client}->status_line,
					    method_name => 'get_generic_dimension_labels',
				       );
    }
}
 


=head2 get_generic_data

  $result = $obj->get_generic_data($params)

=over 4

=item Parameter and return types

=begin html

<pre>
$params is a GenericsUtil.GetGenericDataParams
$result is a GenericsUtil.GetGenericDataResult
GetGenericDataParams is a reference to a hash where the following keys are defined:
	object_id has a value which is a string
	variable_dimension_ids has a value which is a reference to a list where each element is a string
	constant_dimension_ids has a value which is a reference to a hash where the key is a string and the value is an int
GetGenericDataResult is a reference to a hash where the following keys are defined:
	values_x has a value which is a KBaseGenerics.Values
	values_y has a value which is a reference to a list where each element is a KBaseGenerics.Values
	series_labels has a value which is a reference to a list where each element is a string
Values is a reference to a hash where the following keys are defined:
	scalar_type has a value which is a KBaseGenerics.data_type
	object_refs has a value which is a reference to a list where each element is a KBaseGenerics.object_ref
	oterm_refs has a value which is a reference to a list where each element is a KBaseGenerics.oterm_ref
	int_values has a value which is a reference to a list where each element is an int
	float_values has a value which is a reference to a list where each element is a float
	boolean_values has a value which is a reference to a list where each element is a KBaseGenerics.boolean
	string_values has a value which is a reference to a list where each element is a string
data_type is a string
object_ref is a string
oterm_ref is a string
boolean is an int

</pre>

=end html

=begin text

$params is a GenericsUtil.GetGenericDataParams
$result is a GenericsUtil.GetGenericDataResult
GetGenericDataParams is a reference to a hash where the following keys are defined:
	object_id has a value which is a string
	variable_dimension_ids has a value which is a reference to a list where each element is a string
	constant_dimension_ids has a value which is a reference to a hash where the key is a string and the value is an int
GetGenericDataResult is a reference to a hash where the following keys are defined:
	values_x has a value which is a KBaseGenerics.Values
	values_y has a value which is a reference to a list where each element is a KBaseGenerics.Values
	series_labels has a value which is a reference to a list where each element is a string
Values is a reference to a hash where the following keys are defined:
	scalar_type has a value which is a KBaseGenerics.data_type
	object_refs has a value which is a reference to a list where each element is a KBaseGenerics.object_ref
	oterm_refs has a value which is a reference to a list where each element is a KBaseGenerics.oterm_ref
	int_values has a value which is a reference to a list where each element is an int
	float_values has a value which is a reference to a list where each element is a float
	boolean_values has a value which is a reference to a list where each element is a KBaseGenerics.boolean
	string_values has a value which is a reference to a list where each element is a string
data_type is a string
object_ref is a string
oterm_ref is a string
boolean is an int


=end text

=item Description



=back

=cut

 sub get_generic_data
{
    my($self, @args) = @_;

# Authentication: required

    if ((my $n = @args) != 1)
    {
	Bio::KBase::Exceptions::ArgumentValidationError->throw(error =>
							       "Invalid argument count for function get_generic_data (received $n, expecting 1)");
    }
    {
	my($params) = @args;

	my @_bad_arguments;
        (ref($params) eq 'HASH') or push(@_bad_arguments, "Invalid type for argument 1 \"params\" (value was \"$params\")");
        if (@_bad_arguments) {
	    my $msg = "Invalid arguments passed to get_generic_data:\n" . join("", map { "\t$_\n" } @_bad_arguments);
	    Bio::KBase::Exceptions::ArgumentValidationError->throw(error => $msg,
								   method_name => 'get_generic_data');
	}
    }

    my $url = $self->{url};
    my $result = $self->{client}->call($url, $self->{headers}, {
	    method => "GenericsUtil.get_generic_data",
	    params => \@args,
    });
    if ($result) {
	if ($result->is_error) {
	    Bio::KBase::Exceptions::JSONRPC->throw(error => $result->error_message,
					       code => $result->content->{error}->{code},
					       method_name => 'get_generic_data',
					       data => $result->content->{error}->{error} # JSON::RPC::ReturnObject only supports JSONRPC 1.1 or 1.O
					      );
	} else {
	    return wantarray ? @{$result->result} : $result->result->[0];
	}
    } else {
        Bio::KBase::Exceptions::HTTP->throw(error => "Error invoking method get_generic_data",
					    status_line => $self->{client}->status_line,
					    method_name => 'get_generic_data',
				       );
    }
}
 
  
sub status
{
    my($self, @args) = @_;
    if ((my $n = @args) != 0) {
        Bio::KBase::Exceptions::ArgumentValidationError->throw(error =>
                                   "Invalid argument count for function status (received $n, expecting 0)");
    }
    my $url = $self->{url};
    my $result = $self->{client}->call($url, $self->{headers}, {
        method => "GenericsUtil.status",
        params => \@args,
    });
    if ($result) {
        if ($result->is_error) {
            Bio::KBase::Exceptions::JSONRPC->throw(error => $result->error_message,
                           code => $result->content->{error}->{code},
                           method_name => 'status',
                           data => $result->content->{error}->{error} # JSON::RPC::ReturnObject only supports JSONRPC 1.1 or 1.O
                          );
        } else {
            return wantarray ? @{$result->result} : $result->result->[0];
        }
    } else {
        Bio::KBase::Exceptions::HTTP->throw(error => "Error invoking method status",
                        status_line => $self->{client}->status_line,
                        method_name => 'status',
                       );
    }
}
   

sub version {
    my ($self) = @_;
    my $result = $self->{client}->call($self->{url}, $self->{headers}, {
        method => "GenericsUtil.version",
        params => [],
    });
    if ($result) {
        if ($result->is_error) {
            Bio::KBase::Exceptions::JSONRPC->throw(
                error => $result->error_message,
                code => $result->content->{code},
                method_name => 'get_generic_data',
            );
        } else {
            return wantarray ? @{$result->result} : $result->result->[0];
        }
    } else {
        Bio::KBase::Exceptions::HTTP->throw(
            error => "Error invoking method get_generic_data",
            status_line => $self->{client}->status_line,
            method_name => 'get_generic_data',
        );
    }
}

sub _validate_version {
    my ($self) = @_;
    my $svr_version = $self->version();
    my $client_version = $VERSION;
    my ($cMajor, $cMinor) = split(/\./, $client_version);
    my ($sMajor, $sMinor) = split(/\./, $svr_version);
    if ($sMajor != $cMajor) {
        Bio::KBase::Exceptions::ClientServerIncompatible->throw(
            error => "Major version numbers differ.",
            server_version => $svr_version,
            client_version => $client_version
        );
    }
    if ($sMinor < $cMinor) {
        Bio::KBase::Exceptions::ClientServerIncompatible->throw(
            error => "Client minor version greater than Server minor version.",
            server_version => $svr_version,
            client_version => $client_version
        );
    }
    if ($sMinor > $cMinor) {
        warn "New client version available for GenericsUtil::GenericsUtilClient\n";
    }
    if ($sMajor == 0) {
        warn "GenericsUtil::GenericsUtilClient version is $svr_version. API subject to change.\n";
    }
}

=head1 TYPES



=head2 boolean

=over 4



=item Definition

=begin html

<pre>
an int
</pre>

=end html

=begin text

an int

=end text

=back



=head2 File

=over 4



=item Description

Import a CSV file into a NDArray or HNDArray.

"File" and "usermeta" are common to all import methods.


=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
path has a value which is a string
shock_id has a value which is a string

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
path has a value which is a string
shock_id has a value which is a string


=end text

=back



=head2 usermeta

=over 4



=item Definition

=begin html

<pre>
a reference to a hash where the key is a string and the value is a string
</pre>

=end html

=begin text

a reference to a hash where the key is a string and the value is a string

=end text

=back



=head2 ImportCSVParams

=over 4



=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
file has a value which is a GenericsUtil.File
workspace_name has a value which is a string
object_name has a value which is a string
object_type has a value which is a string
metadata has a value which is a GenericsUtil.usermeta

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
file has a value which is a GenericsUtil.File
workspace_name has a value which is a string
object_name has a value which is a string
object_type has a value which is a string
metadata has a value which is a GenericsUtil.usermeta


=end text

=back



=head2 ImportResult

=over 4



=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
object_ref has a value which is a string

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
object_ref has a value which is a string


=end text

=back



=head2 ImportOBOParams

=over 4



=item Description

Import an OBO file into an OntologyDictionary


=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
file has a value which is a GenericsUtil.File
workspace_name has a value which is a string
object_name has a value which is a string
metadata has a value which is a GenericsUtil.usermeta

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
file has a value which is a GenericsUtil.File
workspace_name has a value which is a string
object_name has a value which is a string
metadata has a value which is a GenericsUtil.usermeta


=end text

=back



=head2 ExportParams

=over 4



=item Description

Exporter for generic objects as CSV files


=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
input_ref has a value which is a string

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
input_ref has a value which is a string


=end text

=back



=head2 ExportResult

=over 4



=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
shock_id has a value which is a string

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
shock_id has a value which is a string


=end text

=back



=head2 ListGenericObjectsParams

=over 4



=item Description

List generic objects in one or more workspaces

optional parameters:
allowed_object_types - limits to specific types of object, e.g., KBaseGenerics.NDArray (version number is optional)
allowed_data_types - limits to specific data types, e.g., microbial growth
allowed_scalar_types - limits to specific scalar types, e.g., object_ref, int, float (see KBaseGenerics.spec for valid types).  HNDArrays must have at least one dimension that passes.
min_dimensions - limits to generics with minimum number of dimensions
max_dimensions - limits to generics with max number of dimensions
limit_mapped - if 0 (or unset) returns all objects.  if 1, returns only mapped objects.  if 2, returns only umapped objects


=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
workspace_names has a value which is a reference to a list where each element is a string
allowed_object_types has a value which is a reference to a list where each element is a string
allowed_data_types has a value which is a reference to a list where each element is a string
allowed_scalar_types has a value which is a reference to a list where each element is a string
min_dimensions has a value which is an int
max_dimensions has a value which is an int
limit_mapped has a value which is an int

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
workspace_names has a value which is a reference to a list where each element is a string
allowed_object_types has a value which is a reference to a list where each element is a string
allowed_data_types has a value which is a reference to a list where each element is a string
allowed_scalar_types has a value which is a reference to a list where each element is a string
min_dimensions has a value which is an int
max_dimensions has a value which is an int
limit_mapped has a value which is an int


=end text

=back



=head2 ListGenericObjectsResult

=over 4



=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
object_ids has a value which is a reference to a list where each element is a string

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
object_ids has a value which is a reference to a list where each element is a string


=end text

=back



=head2 GetGenericMetadataParams

=over 4



=item Description

Get metadata describing the dimensions of one or more generic objects


=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
object_ids has a value which is a reference to a list where each element is a string

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
object_ids has a value which is a reference to a list where each element is a string


=end text

=back



=head2 GenericMetadata

=over 4



=item Description

Basic metadata about an object:

object_type - e.g., KBaseGenerics.HNDArray‑4.0
data_type - e.g., microbial growth
n_dimensions - number of dimensions
is_mapped - 0 or 1 indicating mapped status
value_types - list of value types in the object (there will only be 1 for NDArray objects), e.g., "specific activity"
scalar_types - list of scalar types in the object (there will only be 1 for NDArray objects), e.g., "float"
dimension_types - a string describing each dimension (e.g., "media name")
dimension_sizes - size (length) of each dimension
dimension_value_types - a string describing each context of each dimension (e.g., "media name")
dimension_scalar_types - type of values in each context of each dimension (e.g., "int")


=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
object_type has a value which is a string
data_type has a value which is a string
n_dimensions has a value which is an int
is_mapped has a value which is a GenericsUtil.boolean
value_types has a value which is a reference to a list where each element is a string
scalar_types has a value which is a reference to a list where each element is a string
dimension_types has a value which is a reference to a list where each element is a string
dimension_sizes has a value which is a reference to a list where each element is an int
has_subindices has a value which is a reference to a list where each element is a GenericsUtil.boolean
dimension_value_types has a value which is a reference to a list where each element is a reference to a list where each element is a string
dimension_scalar_types has a value which is a reference to a list where each element is a reference to a list where each element is a string

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
object_type has a value which is a string
data_type has a value which is a string
n_dimensions has a value which is an int
is_mapped has a value which is a GenericsUtil.boolean
value_types has a value which is a reference to a list where each element is a string
scalar_types has a value which is a reference to a list where each element is a string
dimension_types has a value which is a reference to a list where each element is a string
dimension_sizes has a value which is a reference to a list where each element is an int
has_subindices has a value which is a reference to a list where each element is a GenericsUtil.boolean
dimension_value_types has a value which is a reference to a list where each element is a reference to a list where each element is a string
dimension_scalar_types has a value which is a reference to a list where each element is a reference to a list where each element is a string


=end text

=back



=head2 GetGenericMetadataResult

=over 4



=item Description

maps object ids to structure with metadata


=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
object_info has a value which is a reference to a hash where the key is a string and the value is a GenericsUtil.GenericMetadata

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
object_info has a value which is a reference to a hash where the key is a string and the value is a GenericsUtil.GenericMetadata


=end text

=back



=head2 GetGenericDimensionLabelsParams

=over 4



=item Description

gets labels for list of dimension axes for a generic object.

User will pass in the numeric indices of all dimensions they care
about (e.g., 1/1 will mean 1st dimension, 1st data type, 2/1 = 2nd
dimension, 1st data type), and an optional flag, convert_to_string.
The API will return a hash mapping each of the dimension indices to
a Values object.  The Values will either contain the scalar type in
the original format, or if the convert_to_string flag is set, will
convert the scalar type to strings.  If unique_values is set, the
API will only return the unique values in each dimension (these will
also be re-indexed, but not resorted, so the Values array may be a
different length).


=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
object_id has a value which is a string
dimension_ids has a value which is a reference to a list where each element is a string
convert_to_string has a value which is a GenericsUtil.boolean
unique_values has a value which is a GenericsUtil.boolean

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
object_id has a value which is a string
dimension_ids has a value which is a reference to a list where each element is a string
convert_to_string has a value which is a GenericsUtil.boolean
unique_values has a value which is a GenericsUtil.boolean


=end text

=back



=head2 GetGenericDimensionLabelsResult

=over 4



=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
dimension_labels has a value which is a reference to a hash where the key is a string and the value is a KBaseGenerics.Values

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
dimension_labels has a value which is a reference to a hash where the key is a string and the value is a KBaseGenerics.Values


=end text

=back



=head2 GetGenericDataParams

=over 4



=item Description

gets subset of generic data as a 2D matrix

Users passes in the dimension indices to use as variables (1st
one must be X axis; additional variables will lead to additional
series being returned).

User selects which dimension indices to fix to
particular constants.  This can be done one of two ways:  either
by fixing an entire dimension (e.g., "2" for the 2nd dimension)
to an index in the complete list
of labels, or by fixing a dimension index (e.g., "2/3" for the
3rd type of values in the 2nd dimension) to an index in the
list of unique labels for that dimension index.

returns:
values_x will contain the list of x-axis values
values_y will contain 1 list of of y-axis values per series.  The number
  of series depends on the number of variable dimensions.
series_labels will show which variable index values correspond
  to which series


=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
object_id has a value which is a string
variable_dimension_ids has a value which is a reference to a list where each element is a string
constant_dimension_ids has a value which is a reference to a hash where the key is a string and the value is an int

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
object_id has a value which is a string
variable_dimension_ids has a value which is a reference to a list where each element is a string
constant_dimension_ids has a value which is a reference to a hash where the key is a string and the value is an int


=end text

=back



=head2 GetGenericDataResult

=over 4



=item Definition

=begin html

<pre>
a reference to a hash where the following keys are defined:
values_x has a value which is a KBaseGenerics.Values
values_y has a value which is a reference to a list where each element is a KBaseGenerics.Values
series_labels has a value which is a reference to a list where each element is a string

</pre>

=end html

=begin text

a reference to a hash where the following keys are defined:
values_x has a value which is a KBaseGenerics.Values
values_y has a value which is a reference to a list where each element is a KBaseGenerics.Values
series_labels has a value which is a reference to a list where each element is a string


=end text

=back



=cut

package GenericsUtil::GenericsUtilClient::RpcClient;
use base 'JSON::RPC::Client';
use POSIX;
use strict;

#
# Override JSON::RPC::Client::call because it doesn't handle error returns properly.
#

sub call {
    my ($self, $uri, $headers, $obj) = @_;
    my $result;


    {
	if ($uri =~ /\?/) {
	    $result = $self->_get($uri);
	}
	else {
	    Carp::croak "not hashref." unless (ref $obj eq 'HASH');
	    $result = $self->_post($uri, $headers, $obj);
	}

    }

    my $service = $obj->{method} =~ /^system\./ if ( $obj );

    $self->status_line($result->status_line);

    if ($result->is_success) {

        return unless($result->content); # notification?

        if ($service) {
            return JSON::RPC::ServiceObject->new($result, $self->json);
        }

        return JSON::RPC::ReturnObject->new($result, $self->json);
    }
    elsif ($result->content_type eq 'application/json')
    {
        return JSON::RPC::ReturnObject->new($result, $self->json);
    }
    else {
        return;
    }
}


sub _post {
    my ($self, $uri, $headers, $obj) = @_;
    my $json = $self->json;

    $obj->{version} ||= $self->{version} || '1.1';

    if ($obj->{version} eq '1.0') {
        delete $obj->{version};
        if (exists $obj->{id}) {
            $self->id($obj->{id}) if ($obj->{id}); # if undef, it is notification.
        }
        else {
            $obj->{id} = $self->id || ($self->id('JSON::RPC::Client'));
        }
    }
    else {
        # $obj->{id} = $self->id if (defined $self->id);
	# Assign a random number to the id if one hasn't been set
	$obj->{id} = (defined $self->id) ? $self->id : substr(rand(),2);
    }

    my $content = $json->encode($obj);

    $self->ua->post(
        $uri,
        Content_Type   => $self->{content_type},
        Content        => $content,
        Accept         => 'application/json',
	@$headers,
	($self->{token} ? (Authorization => $self->{token}) : ()),
    );
}



1;
