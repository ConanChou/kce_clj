# Kindle Clipping Extractor in Clojure (kce_clj)

A simple library to extract Kindle generated `clipping.txt`.

[![Clojars Project](https://img.shields.io/clojars/v/kce-clj.svg)](https://clojars.org/kce-clj)

## Usage

kce_clj comes with 2 flavor. A hard-working version and a lazy version:

```
> (time (nth (extract "clipping.txt" "EST") 42))
"Elapsed time: 35.68178 msecs"
{:category :highlight,
 :page {:start 7, :end 7},
 :title "Dance Music Manual: Tools, Toys, and Techniques",
 :author "Snoman, Rick",
 :timestamp #inst "2016-06-30T15:21:52.000-00:00",
 :location {:start 274, :end 275},
 :text "The harmonic minor scale is very similar to the natural minor scale but with the seventh note increased, or augmented, up the scale by a semitone."}

> (time
    (with-open [rdr (clojure.java.io/reader "clipping.txt")]
      (nth (lazy-extract rdr "EST") 42)))
"Elapsed time: 6.201403 msecs"
{:category :highlight,
 :page {:start 7, :end 7},
 :title "Dance Music Manual: Tools, Toys, and Techniques",
 :author "Snoman, Rick",
 :timestamp #inst "2016-06-30T15:21:52.000-00:00",
 :location {:start 274, :end 275},
 :text "The harmonic minor scale is very similar to the natural minor scale but with the seventh note increased, or augmented, up the scale by a semitone."}
```

As you can see, you need to a bit more hard-working if you want to your code to be lazy :)

Usage:

```
(extract file_path_string tz_id)
(lazy-extract file_reader tz_id)
```

`tz_id` is java `TimeZone` ID. `file_reader` should be a `clojure.java.io/reader`.



## License

MIT
