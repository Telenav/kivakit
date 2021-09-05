#!/usr/bin/perl

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

use strict;
use warnings FATAL => 'all';

#
# Include build script from cactus-build
#

system("git clone --branch develop --quiet https://github.com/Telenav/cactus-build.git");

require "./cactus-build/.github/scripts/build-include.pl";

#
# Get build type and branch
#

my ($build_type, $reference) = @ARGV;
my $branch = reference_to_branch($reference);

check_build_type($build_type);
check_branch($branch);

say("Building $branch ($build_type)");

#
# Clone repositories
#

my $github = "https://github.com/Telenav";

clone("$github/kivakit", $branch, "allow-pull-request");

#
# Build repositories
#

build_kivakit($build_type);

