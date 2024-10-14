# Проблема с версией долями 2.0.7

При повышение версии либы долей до 2.0.7 не собирается билд с включенным `isMinifyEnabled`.
```
AGPBI: {
  "kind": "error",
  "text": "java.nio.file.NoSuchFileException: /Users/~/dolyame-test/sdk-sources/internal/build/outputs/mapping/release/mapping.txt",
  "sources": [
    {
      "file": "/Users/~/dolyame-test/sdk-sources/internal/build/outputs/mapping/release/mapping.txt"
    }
  ],
  "tool": "R8"
}
```
На версии 2.0.3 такой проблемы не наблюдается.

В этом тестовом проекте `isMinifyEnabled = true` задан у билда с типом release, поэтому тестировать с release вариантом.
