# Kindle Clipping Extractor in Clojure (kce-clj)

A simple library to extract Kindle generated `clipping.txt`.

[![Clojars Project](https://img.shields.io/clojars/v/kce-clj.svg)](https://clojars.org/kce-clj)

## Usage

kce_clj comes with 2 flavors. A hard-working version and a lazy version:

```
λ (time (nth (extract "clipping.txt" "EST") 42))
"Elapsed time: 35.68178 msecs"
{:category :highlight,
 :page {:start 7, :end 7},
 :title "Dance Music Manual: Tools, Toys, and Techniques",
 :author "Snoman, Rick",
 :timestamp #inst "2016-06-30T15:21:52.000-00:00",
 :location {:start 274, :end 275},
 :text "The harmonic minor scale is very similar to the natural minor scale
 but with the seventh note increased, or augmented, up the scale by a
 semitone."}

λ (time
    (with-open [rdr (clojure.java.io/reader "clipping.txt")]
      (nth (lazy-extract rdr "EST") 42)))
"Elapsed time: 6.201403 msecs"
{:category :highlight,
 :page {:start 7, :end 7},
 :title "Dance Music Manual: Tools, Toys, and Techniques",
 :author "Snoman, Rick",
 :timestamp #inst "2016-06-30T15:21:52.000-00:00",
 :location {:start 274, :end 275},
 :text "The harmonic minor scale is very similar to the natural minor scale
 but with the seventh note increased, or augmented, up the scale by a
 semitone."}
```

As you can see, you need to be a bit more hard-working if you want your code to be lazy :)

### Parameters

```
(extract file_path_string tz_id)
(lazy-extract file_reader tz_id)
```

`tz_id` is a java `TimeZone` ID. `file_reader` should be a `clojure.java.io/reader`.



## License

MIT
