var convertToJSON = function(data, origin) {
  return (origin == 'cloc') ? convertFromClocToJSON(data) : convertFromWcToJSON(data);
};

/**
 * Convert the output of cloc in csv to JSON format
 *
 *  > cloc . --csv --exclude-dir=vendor,tmp --by-file --report-file=data.cloc
 */
var convertFromClocToJSON = function(data) {
  var lines = data.split("\n");
  lines.shift(); // drop the header line

  var json = {};
  lines.forEach(function(line) {
    var cols = line.split(',');
    var filename = cols[1];
    if (!filename) return;
    var elements = filename.split(/[\/\\]/);
    var current = json;
    elements.forEach(function(element) {
      if (!current[element]) {
        current[element] = {};
      }
      current = current[element];
    });
    current.codelang = cols[0];
    current.size = parseInt(cols[4], 10);
  });

  json = getChildren(json)[0];
  json.name = 'root';

  return json;
};

/**
 * Convert the output of wc to JSON format
 *
 *  > git ls-files | xargs wc -l
 */
var convertFromWcToJSON = function(data) {
  var lines = data.split("\n");

  var json = {};
  var filename, size, cols, elements, current;
  lines.forEach(function(line) {
      cols = line.trim().split(' ');
      size = parseInt(cols[0], 10);
      if (!size) return;
      filename = cols[1];
      if (filename === "total") return;
      if (!filename) return;
      elements = filename.split(/[\/\\]/);
      current = json;
      elements.forEach(function(element) {
          if (!current[element]) {
              current[element] = {};
          }
          current = current[element];
      });
      current.size = size;
  });

  json.children = getChildren(json);
  json.name = 'root';
  // console.log(json);
  return json;
};

/**
 * Convert a simple json object into another specifying children as an array
 * Works recursively
 *
 * example input:
 * { a: { b: { c: { size: 12 }, d: { size: 34 } }, e: { size: 56 } } }
 * example output
 * { name: a, children: [
 *   { name: b, children: [
 *     { name: c, size: 12 },
 *     { name: d, size: 34 }
 *   ] },
 *   { name: e, size: 56 }
 * ] } }
 */
var getChildren = function(json) {
  var children = [];
  if (json.codelang) return children;
  for (var key in json) {
    var child = { name: key };
    if (json[key].size) {
      // value node
      child.size = json[key].size;
      child.codelang = json[key].codelang;
    } else {
      // children node
      var childChildren = getChildren(json[key]);
      if (childChildren) child.children = childChildren;
    }
    children.push(child);
    delete json[key];
  }

 return children;
};

// Recursively count all elements in a tree
var countElements = function(node) {
  var nbElements = 1;
  if (node.children) {
    nbElements += node.children.reduce(function(p, v) { return p + countElements(v); }, 0);
  }
  return nbElements;
};


var fs = require('fs');

if (process.argv.length !== 3) {
    console.error('Exactly one argument required');
    process.exit(1);
}

var input = process.argv[2] + '.wc';
var output = process.argv[2] + '.json';

// Read the entire file asynchronously, with a callback to replace the r's and l's
// with w's then write the result to the new file.
fs.readFile(input, 'utf-8', function (err, text) {
    if (err) throw err;
    if (text.trim())
    {
    	// console.log('json ' + input + ' --> ' + output);
	    var json = convertToJSON(text, 'wc');
	    fs.writeFile(output, JSON.stringify(json), function (err) {
	        if (err) throw err;
	    });
    }
});
