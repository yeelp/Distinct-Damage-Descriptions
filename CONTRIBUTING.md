# Contributing
![GitHub forks](https://img.shields.io/github/forks/yeelp/Distinct-Damage-Descriptions)

![GitHub License](https://img.shields.io/github/license/yeelp/Distinct-Damage-Descriptions)
![](https://img.shields.io/github/v/release/yeelp/Distinct-Damage-Descriptions?include_prereleases)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/yeelp/Distinct-Damage-Descriptions/latest?include_prereleases)
[![](https://img.shields.io/github/issues/yeelp/Distinct-Damage-Descriptions)](https://github.com/yeelp/Distinct-Damage-Descriptions/issues)
![GitHub issues by-label](https://img.shields.io/github/issues/yeelp/Distinct-Damage-Descriptions/help%20wanted?style=flat)
![GitHub pull requests](https://img.shields.io/github/issues-pr-raw/yeelp/Distinct-Damage-Descriptions)

![GitHub repo size](https://img.shields.io/github/repo-size/yeelp/Distinct-Damage-Descriptions?color=purple)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/yeelp/Distinct-Damage-Descriptions?color=purple)

Distinct Damage Descriptions (or DDD) openly welcomes all contributors. Before you contribute though, you should familiarize yourself with the following guidelines. Note that these contributing guidelines may be updated in the future.

## Terminology
**Contributor**: someone who has contributed to the DDD repo in the past. You can see a list [here](https://github.com/yeelp/Distinct-Damage-Descriptions/graphs/contributors).

**Author**: The owner of the repository, Yeelp.

**PR**: Pull Request. GitHub veterans know what this is, but this document assumes no prior knowledge. The simple description is that your PR is what you are offering to contribute to the repository.

**Issue**: The issue you are raising in the [Issue Tracker](https://github.com/yeelp/Distinct-Damage-Descriptions/issues).

**Contribution**: What you are contributing, be it PR or Issue.

## General Guidelines
Distinct Damage Descriptions' contributing guidelines build upon [GitHub's Contribution Guidelines](https://docs.github.com/en/site-policy/github-terms/github-community-guidelines). You should familiarize yourself with those first. The important takeaways from those guidelines are:
- **Be open-minded**: Contributors and the Author may offer suggestions or feedback on your Contribution. This is not intended to be elitist. This is solely to offer ways to improve the Contribution in some way. Likewise, Contributors and the Author will be open-minded to your Contribution! It is a two way street.
- **Be respectful**: Discourse in a Contribution should be done in respectful and engaging manner. Much like how you spent time and effort on your Contribution, Contributors and especially the Author spent a lot of time and effort to make DDD what it currently is. Respect the effort, and your effort will be respected as well.
- **Remain on topic**: Discourse in a Contribution should be about the Contribution. A side comment here or there is fine, but if the discussion veers off course, the conversation will be locked.

### PR Guidelines
The PR Guidelines for DDD mainly focus around code style and scope. Before you begin to work on a PR, check if a similar open PR already exists first. If you're not sure if contributing something will fall within guidelines or scope, you can ask on Discord under `#ddd-development-discussion`.

PR's that contribute code should contribute code that matches the coding style of DDD. In particular, opening braces for code blocks should be on the same line, and code should be properly indented. If statements with one line bodies should still have opening and closing braces, and the body should be on its own line like so
```java
if(somethingIsTrue()) {
  doSomethingAsAResult();
}
```
This applies not only to if blocks, but all blocks of code. Any code that does not follow this style will have this change encouraged and may not be merged until such change is made.

If a line of code gets too long, it may be split across multiple lines at `.` characters. However, the `//@formatter:off` and `//@formatter:on` comments should surround the lines where this happens.

PR's should remain in scope of what DDD is. PR's that add new things like blocks, ores or items have a ***very high*** chance of not being merged. DDD is a mechanics mod first and foremost.

### Issue Guidelines
Follow any issue templates that are relevent. Also double check if a similar issue already exists! This means checking both open *and* closed issues. Duplicate issues will be closed, no questions asked. This is to keep the Issue Tracker clean. If you have something you wish to add to a Issue that already exists, go comment on it (even if closed)!

#### Bug Reports
Make sure to include the mod version, and any other mods that are present. In addition, clearly state what the bug was, including what happened and what was expected. Lastly, you really should include steps to reproduce the bug! Opening a bug report that can not be reproduced, or a bug report where steps to reproduce aren't included is not always helpful. Having steps to reproduce makes diagnosing the issue much easier!

If you open a bug report that doesn't include steps to reproduce, you are almost certainly going to be asked to include steps to reproduce.

Make sure you're clear and concise. If you write in your Issue that a bug happens sometimes, then you should specify the conditions where it happens and where it doesn't. If it happens all the time, say so.

The more information you can include, the better!

#### Feature Requests
Feature requests for DDD should be within the scope of the mod. New blocks, mobs, items, etc. are not in scope requests for those are very likely to be closed as a result.

The more specific you can be about your feature request, the better the discussion around your feature request will be.

##### Mod Integration/Compatibility Requests
If you are making any feature request for new mod intergration or compatibility, you should check the Discord's `#ddd-official-mod-integration-list` channel to know which mods have been added/planned/considered/denied. Denied mods are unlikely to be reconsidered.
