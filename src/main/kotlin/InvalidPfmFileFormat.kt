package org.example

/**
 * Exception class for handling invalid PFM file format errors.
 * @errormsg = the error message describing the problem (default is null)
 */
class InvalidPfmFileFormat(private var errormsg:String?=null): Exception(errormsg)
