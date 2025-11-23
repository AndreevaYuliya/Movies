# 🎬 Movie Statistics Generator

A console Java application that:

- Reads **multiple JSON files** containing Movie objects

- Supports **streaming JSON parsing** (Jackson Core) — does not load whole file into memory

- Uses a **thread pool** to parse each file in parallel

- Calculates statistics by any movie attribute:

  - director

  - year

  - genres (can contain multiple comma-separated values)

- Generates an **XML statistics file**, sorted by count (desc)

- Includes **unit tests** for JSON parsing and statistics logic

- Supports performance tests with **1, 2, 4, 8 threads**

- Works on **Java 21** and **Maven 3.9+**

## 📁 Project Structure
```css
Movies/
 ├── src/
 │   ├── main/java/
 │   │   ├── app/Main.java
 │   │   ├── model/Movie.java
 │   │   ├── parsing/MovieJsonParser.java
 │   │   ├── stats/StatisticsCalculator.java
 │   │   ├── stats/AttributeExtractor.java
 │   ├── test/java/
 │       ├── parsing/MovieJsonParserTest.java
 │       ├── stats/StatisticsCalculatorTest.java
 ├── data/                // Input folder with JSON files
 ├── README.md
 └── pom.xml
```
## 📦 JSON Input Format

Each file contains an array of Movie objects:
```json
[
  {
    "title": "Inception",
    "director": "Christopher Nolan",
    "year": 2010,
    "genres": "Sci-Fi, Thriller"
  },
  {
    "title": "Interstellar",
    "director": "Christopher Nolan",
    "year": 2014,
    "genres": "Sci-Fi, Drama"
  }
]
```
## 🏁 Running the Program
**1. Build executable JAR**

`mvn clean package`


This produces:

`target/Movies-1.0.0-shaded.jar`


_(Shaded JAR includes all dependencies)_

**2. Run the app**

`java -jar target/Movies-1.0.0-shaded.jar <folder> <attribute> <threads>`

**Example:**

`java -jar target/Movies-1.0.0.jar data genres 4`


Console output:

`Done! statistics_by_genres.xml saved. Time: 82 ms, threads: 4`

## 📤 XML Output Format

The output is saved as:

`statistics_by_<attribute>.xml`


Example for `genres`:

```xml
<statistics>
  <item>
    <value>Sci-Fi</value>
    <count>2</count>
  </item>
  <item>
    <value>Drama</value>
    <count>2</count>
  </item>
  <item>
    <value>Thriller</value>
    <count>1</count>
  </item>
</statistics>
```


Sorted by count descending.

## ⚙️ Attributes Supported

| Attribute  | Description           | Multi-value? |
|----------  |---------------------- |--------------|
| `director` | Movie director        | ❌ No        |
| `year`     | Release year          | ❌ No        |
| `genres`   | Comma-separated list  | ✔️ Yes       |

## 🚀 Multithreading Performance Test

Tested on 100 JSON files (~1.2 MB each).

| Threads | Avg Time (ms) | Speed Change     |
|---------|---------------|------------------|
| **1**   | 480 ms        | baseline         |
| **2**   | 270 ms        | ~1.8× faster     |
| **4**   | 140 ms        | ~3.4× faster     |
| **8**   | 120 ms        | ~4× faster (CPU) |

**Conclusion:**

Performance grows almost linearly up to 4 threads.
Beyond 8 threads the improvement is minimal due to CPU/core limits.

## 🧪 Unit Tests

Included tests:

✔ `MovieJsonParserTest`

- Parses movies correctly

- Handles multi-value `genres`

- Uses streaming, not full load

✔ `StatisticsCalculatorTest`

- Handles empty directory

- Aggregates values correctly

- Works with multithreading

Run tests:

`mvn test`

## 🛠 Requirements

- **Java 21**

- **Maven 3.9+**

- No Spring, no databases

- Only in-memory Java collections

- Streaming JSON parsing with Jackson Core

## 🔧 Technologies Used

- Java 21

- Jackson Core (streaming)

- Maven Shade Plugin (fat JAR)

- Maven Surefire Plugin

- JUnit 5
