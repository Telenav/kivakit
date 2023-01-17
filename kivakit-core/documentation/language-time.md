# kivakit-core-kernel language.time   ![](https://telenav.github.io/telenav-assets/images/icons/clock-40.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.time*

### Index

[**Summary**](#summary)  
[**Time**](#time)  
[**Duration**](#duration)  
[**LocalTime**](#localtime)  
[**LocalTime Formatting**](#localtime-formatting)  
[**LocalTime Parsing**](#localtime-parsing)  
[**Command Line Switches**](#command-line-switches)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

This package provides several handy wrappers for working with time values that simplify the Java time  
API, provide new functionality and format times and dates in a manner consistent with the UNIX, macOS  
and Windows operating systems. These classes also provide consistent integration with the rest of the  
KivaKit, so you will find them used throughout.

### Time <a name="time"></a>

The _Time_ class is similar to the Java _Instant_ class, but with a different approach to its API.  
You can create a time by specifying the current time, or a time in common time units from the start  
of UNIX time as follows. Many KivaKit API methods also return _Time_ objects, such as _File.lastModified()_.

    Time now = Time.now();
    Time time = Time.milliseconds(5);
    Time time = Time.seconds(30);
    Time modified = file.lastModified();

Time units can be compared, added, subtracted and converted into _Duration_ objects, which represent  
a span of time, in various ways:

    Time start = Time.now();
    Duration elapsed = Time.now().subtract(start);
    Duration elapsed = start.elapsedSince();
    Time future = file.lastModified().add(Duration.hours(5));
    Duration waitTime = future.fromNow();
    assert future.isAfter(start);

The _Time.toString()_ method returns the time in local time in KivaKit format. More on this below.

### Duration <a name="duration"></a>

Durations can also be constructed from common time units as well as being returned from KivaKit API methods.

    Duration.milliseconds(100);
    Duration.hours(5);
    Duration.seconds(15);
    Duration.days(1);

Like _Time_ objects, _Durations_ can also be compared, added, subtracted. They can also be multiplied,  
divided, etc...

    sleepTime.plus(Duration.minutes(5));
    sleepTime.dividedBy(3);
    Duration.seconds(4).maximum(Duration.seconds(5));
    sleepTime.longer(Percentage.of(50));
    if (sleepTime.isLessThan(Duration.seconds(1))) { ... }

The _Duration.toString()_ method returns the duration in a human-readable form:

    5 seconds 100 milliseconds 1.5 days 37.5 minutes

This form can also be parsed by _Duration.parse(String)_.

### LocalTime <a name="localtime"></a>

The _LocalTime_ object is essentially an instant (_Time_) in a particular Java _TimeZone_. You can  
easily get a _LocalTime_ from a _Time_ object in a few different ways:

    LocalTime local = Time.now().localTime();
    LocalTime local = Time.now().localTime(zone);
    LocalTime local = file.lastModified().localTime("America/Los Angeles");
    LocalTime local = Time.now().utc();

Once you have a _LocalTime_ object, you can do a lot.

You can get various time zone relative values:

    int day = local.day()
    int month = local.month()
    int hour = local.hour()
    Meridiem ampm = local.meridiem()
    LocalTime nextHour = local.startOfHour();
    LocalTime tommorow = local.startOfTomorrow();

### LocalTime Formatting <a name="localtime-formatting"></a>

_LocalTime_ objects can be formatted and parsed and used as command line switches in standardized KivaKit  
formats which are acceptable as filenames on Windows, macOS and UNIX. This is important because when  
the Graph API creates files and folders, it may use the time and/or date as part of the output file  
name to make it easier to recognize what is in the file. For example:

    HERE-UniDb-PBF-North_America-2020.04.01_04.01PM_PST.osm.pbf

From this filename you can tell that North American data was provided by HERE under the UniDB  
specification in PBF format. The data version is given by the date.

If you need to convert a *LocalTime* to KivaKit formats, you have several options, including:

    local.kivaTimeString()      // 4.01PM_PST 
    local.kivaDateString()      // 2020.04.01 
    local.kivaDateTimeString()  // 2020.04.01_4.01PM_PST 

### LocalTime Parsing <a name="localtime-parsing"></a>

Parsing is easy as well:

    LocalTime time = LocalTime.parseKivaTime(listener, "4.01PM_PST");
    LocalTime time = LocalTime.parseKivaTime(listener, "4.01PM_UTC");

### Command Line Switches <a name="command-line-switches"></a>

Command line parsing of KivaKit _LocalTime_ is very easy. For more details, see the section on command  
line parsing.

    private static final SwitchParser<LocalTime> DATA_DATE = 
        LocalTime.kivaDateSwitchParser(LocalTime.utcTimeZone(), "data-date",
            "The date of the input PBF file as yyyy.MM.dd (for example, 2017.06.01)")
        .required()
        .build();

    public static void main( String[] arguments)
    { 
        CommandLine commandLine = new CommandLineParser.add(DATA_DATE).parser(arguments);
        LocalTime dataDate = commandLIne.get(DATA_DATE);
    }

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
