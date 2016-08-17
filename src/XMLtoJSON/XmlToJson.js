/**
 * 
 */
function(xml) {
 var x2js = new X2JS();
 var json = x2js.xml_str2json( xml );
 return json;
}