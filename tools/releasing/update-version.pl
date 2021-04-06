#!/usr/bin/perl

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

use strict;
use warnings;
use File::Find;
use File::Slurp;

#
# Check arguments
#
if (@ARGV != 3) {
    die "Usage: [root-folder] [old-version] [new-version]";
}

#
# Get arguments
#
my $root = shift @ARGV;
my $old_version = shift @ARGV;
my $new_version = shift @ARGV;

#
# Get base version from new version by removing any -SNAPSHOT suffix
#
my $base_version = $new_version;
$base_version =~ s/-SNAPSHOT//;

#
# Begin updating
#
print "Updating $root from $old_version to $new_version\n";

#
# Update markdown file if it contains the old version
#
sub update_markdown {
    my $path = shift @_;
    my $text = read_file($path);
    my $updated = $text;
    $updated =~ s/\b${old_version}\b/${new_version}/g;

    if ($text ne $updated) {
        print "Updating $path\n";
        write_file($path, $updated);
    }
}

#
# Update the pom file version
#
sub update_pom {
    my $path = shift @_;

    my $text = read_file($path);

    my $updated = $text;
    $updated =~ s#^\s+<parent>\s+<groupId>(.*?)</groupId>\s+<artifactId>(.*?)</artifactId>\s+<version>(.*?)</version>\s+</parent>#qq!    <parent>
        <groupId>$1</groupId>
        <artifactId>$2</artifactId>
        <version>$new_version</version>
    </parent>!#esmg;

    $updated =~ s#    <version>${old_version}</version>#qq!    <version>${new_version}</version>!#esmg;

    if ($text ne $updated) {
        print "Updating $path\n";
        write_file($path, $updated);
    }
}

#
# Process file from all files under the root
#
sub process_file {
    my $path = $File::Find::name;

    if ($path =~ /pom.xml/g) {
        update_pom($path);
    }

    if ($path =~ /.*\.md$/) {
        update_markdown($path);
    }
}

#
# Update the root pom
#
sub update_root_pom {
    my $path = "$root/pom.xml";
    my $text = read_file($path);
    $text =~ s!<version>(?<version>.*?)</version>!<version>$new_version</version>!;
    $text =~ s!<kivakit.version>(?<version>.*?)</kivakit.version>!<kivakit.version>$new_version</kivakit.version>!;
    $text =~ s!<kivakit.base.version>(?<version>.*?)</kivakit.base.version>!<kivakit.base.version>$base_version</kivakit.base.version>!;
    write_file($path, $text);
    print "Updated root pom.xml\n";
}

#
# Process all files in the project
#
chdir $root;
find(\&process_file, $root);

#
# Update the root pom
#
update_root_pom();
