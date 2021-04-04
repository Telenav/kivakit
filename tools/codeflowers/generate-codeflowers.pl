#!/usr/bin/perl

use File::Find;
use File::Slurp;

$arguments = $#ARGV + 1;

if ($arguments != 2) {
    die "generate-codeflowers.pl source target";
}

$source = shift @ARGV;
$destination = shift @ARGV;

sub artifactId {
    my ($text) = shift @_;
    $text =~ s!<artifactId>(.+?)</artifactId>!!;
    $text =~ m!<artifactId>(.+?)</artifactId>!;
    $1;
}
sub packaging {
    my ($text) = shift @_;
    $text =~ m!<packaging>(.+?)</packaging>!;
    $1;
}

@codeflowers = ();

sub generateCodeFlower {
    my ($folder) = $_;
    if (-d && $_ ne '.' && $_ ne '..' && -e "$folder/pom.xml") {
        $pom = read_file("$folder/pom.xml");
        $artifact = &artifactId($pom);
        if (&packaging($pom) eq 'jar') {
            mkdir $destination;
            $input = "$folder/src/main/\\*.java";
            $output = "$destination/$artifact.wc";
            # print "processing $artifact\n";
            system("git ls-files $input | xargs wc -l | sed -e 's/[^ 0-9]*\\/src\\/main\\/java\\///g' > $output");
            system("node $ENV{KivaKit_HOME}/tools/codeflowers/site/dataConverter.js $destination/$artifact");
            push(@codeflowers, "<option value='data/$artifact.json'>$artifact</option>\n");
        }
    }
}

find(\&generateCodeFlower, $source);

@codeflowers = sort(@codeflowers);
print "@codeflowers";

