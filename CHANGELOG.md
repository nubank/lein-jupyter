# Changelog

## 0.2.24-NUBANK-6
- Fix NPE when getting the jupyterlab version for nbclassic;

## 0.2.24-NUBANK-5
- Add support for nbclassic when clojupyter >= 4.0.0
- Abort the execution when cannot install kernel and extensions;

## 0.2.24-NUBANK-4
- Bump clojupyter version to 0.3.6;

## 0.2.24-NUBANK-3
- Bump clojupyter version to 0.3.3;
- Change clojupyter dependency back to the original repo instead of nubank's fork;

## 0.1.24-NUBANK
- Upgrade `dev.nubank/clojupyter` to verion 0.3.3-alpha2-NUBANK

## 0.1.23
- Use `dev.nubank/clojupyter` fork on version `0.3.2-fix1`
- Remove pinned dependencies

## 0.1.22
- Upgrade `cheshire` to version `5.10.0` again
- Pin `com.fasterxml.jackson.*` libraries to version `2.10.2`

## 0.1.21
- Downgrade `cheshire` to version `5.8.1`

## 0.1.20
- Bump `clojupyter` to version `0.3.2`

## 0.1.19
- Bump `clojupyter` to version `0.3.1`

## 0.1.18
- Bump libs
- Go back to upstream `clojupyter`, since they have a `clojars` release now

## 0.1.17
- First version after fork: https://github.com/clojupyter/lein-jupyter
- Make it compatible with `clojupyter` `0.2.2` (Nubank's fork)
