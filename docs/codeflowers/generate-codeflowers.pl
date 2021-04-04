#!/usr/bin/perl

use strict;
use warnings;

use File::Find;
use File::Slurp;

my $arguments = $#ARGV + 1;

if ($arguments != 2) {
    die "generate-codeflowers.pl source target";
}

my $source = shift @ARGV;
my $destination = shift @ARGV;

mkdir $destination;

print "Source: $source\n";
print "Destination: $destination\n";

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

my @codeflowers = ();

sub generateCodeFlower {
    my $folder = $_;
    if (-d && $_ ne '.' && $_ ne '..' && -e "$folder/pom.xml") {
        my $pom = read_file("$folder/pom.xml");
        my $artifact = &artifactId($pom);
        if (&packaging($pom) eq 'jar') {
            my $input = "$folder/src/main/\\*.java";
            my $output = "$destination/site/data/$artifact.wc";
            print "Writing word count ${artifact} -> $output\n";
            my $command = "git ls-files $input | xargs wc -l | sed -e 's/[^ 0-9]*\\/src\\/main\\/java\\///g' > $output";
            system($command);
            system("node $destination/site/dataConverter.js $destination/site/data/$artifact");
            push(@codeflowers, "<option value='data/$artifact.json'>$artifact</option>\n");
        }
    }
}

find(\&generateCodeFlower, $source);

@codeflowers = sort(@codeflowers);
print "@codeflowers";

