# How to contribute to Contextium

First off, thanks for taking the time to contribute!

All types of contributions are encouraged and valued. See
the [Table of Contents](#table-of-contents) for different ways to help and details about how this
project handles them. Please make sure to read the relevant section before making your contribution.
It will make it a lot easier for the maintainers and smooth out the experience for all involved.

> And if you like the project, but just don't have time to contribute, that's fine. There are other
> easy ways to support the project and show your appreciation, which we would also be very happy
> about:
> - Star the project
> - Tweet about it
> - Refer this project in your project's readme
> - Mention the project at local meetups and tell your friends/colleagues

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [I Have a Question](#i-have-a-question)
    - [I Want To Contribute](#i-want-to-contribute)
    - [Finding A Good First Issue](#finding-a-good-first-issue)
    - [Reporting Bugs](#reporting-bugs)
    - [Suggesting Enhancements](#suggesting-enhancements)
    - [Your First Code Contribution](#your-first-code-contribution)
    - [Contextium Basic Knowledge](#contextium-basic-knowledge)
    - [Improving The Documentation](#improving-the-documentation)
- [Styleguides](#styleguides)
    - [Commit Messages](#commit-messages)
    - [Code Comments](#code-comments)

## Code of Conduct

This project and everyone participating in it is governed by the
[Contextium Code of Conduct](https://github.com/xcy7e/Contextium/blob/master/CODE_OF_CONDUCT.md).
By participating, you are expected to uphold this code. Please report unacceptable behavior
to <john@xcy7e.de>.

## I Have a Question

> If you want to ask a question, it's assumed that you have read the
> available [Documentation](https://github.com/xcy7e/Contextium/blob/master/README.md).

Before you ask a question, it is best to search for
existing [Issues](https://github.com/xcy7e/Contextium/issues) that might help you. In case you have
found a suitable issue and still need clarification, you can write your question in this issue.

If you then still feel the need to ask a question and need clarification, you are recommended to:

- Open an [Issue](https://github.com/xcy7e/Contextium/issues/new).
- Provide as much context as you can about what you're running into.
- Provide project and platform versions (e.g. android version), depending on what seems relevant.

As for now, I am the only maintainer of Contextium. Therefore it can take some time to respond to
your issue.

<!--
You might want to create a separate issue tag for questions and include it in this description. People should then tag their issues accordingly.

Depending on how large the project is, you may want to outsource the questioning, e.g. to Stack Overflow or Gitter. You may add additional contact and information possibilities:
- Stack Overflow tag
- Blog
- FAQ
- Roadmap
- E-Mail List
-->

## I Want To Contribute

> ### Legal Notice
> When contributing to this project, you must agree that you have authored 100% of the content, that
> you have the necessary rights to the content and that the content you contribute may be provided
> under the project licence.

### Finding A Good First Issue

To get started and familiarize yourself with the project, take a look at unsolved issues labeled
`good first issue`. These have been reviewed by a maintainer and identified as great beginner
challenges.

Please make sure to assign the issue to yourself before starting work so others know it is being
addressed. Any questions or clarifications should be discussed directly within the issue thread to
keep the pull request focused on the code review itself.

### Reporting Bugs

#### Before Submitting a Bug Report

A good bug report shouldn't leave others needing to chase you up for more information. Please
investigate carefully, collect information and describe the issue in detail in your
report. Please complete the following steps in advance to help fix any potential bug as fast as
possible.

- Make sure that you are using the latest version.
  You'll always find the latest version on github, whereas other sources (e.g. F-Droid) may lag
  behind for some time.
- Determine if your bug is really a bug and not an error on your side e.g. using incompatible
  environment components/versions (Make sure that you have read
  the [documentation](https://github.com/xcy7e/Contextium/blob/master/README.md). If you are looking
  for support, you might want to check [this section](#i-have-a-question)).
- To see if other users have experienced (and potentially already solved) the same issue you are
  having, check if there is not already a bug report existing for your bug or error in
  the [bug tracker](https://github.com/xcy7e/Contextium/issues?q=label%3Abug).
- Also make sure to search the internet (including Stack Overflow) to see if users outside of the
  GitHub community have discussed the issue.
- Collect information about the bug:
    - Stack trace (Traceback)
    - OS, Platform and Version (Android, x86, ARM)
    - Version of the interpreter, compiler, SDK, runtime environment, package manager, depending on
      what seems relevant.
    - Possibly your input and the output
    - Can you reliably reproduce the issue? And can you also reproduce it with older versions?

#### How Do I Submit a Good Bug Report?

> You must never report security related issues, vulnerabilities or bugs including sensitive
> information to the issue tracker, or elsewhere in public. Instead sensitive bugs must be sent by
> email to <security@xcy7e.de>.
<!-- You may add a PGP key to allow the messages to be sent encrypted as well. -->

GitHub issues is used only to track bugs and errors. If you run into an issue with the project:

- Open an [Issue](https://github.com/xcy7e/Contextium/issues/new). (Since it can't be ensured at
  this point whether it is a bug or not, please do not to talk about a bug yet and not to label the
  issue.)
- Explain the behavior you would expect and the actual behavior.
- Please provide as much context as possible and describe the *reproduction steps* that someone else
  can follow to recreate the issue on their own. This usually includes your code. For good bug
  reports you should isolate the problem and create a reduced test case.
- Provide the information you collected in the previous section.

Once it's filed:

- Maintainers will label the issue accordingly.
- A maintainer will try to reproduce the issue with your provided steps. If there are no
  reproduction steps or no obvious way to reproduce the issue, you will be asked for those steps
  and the issue is marked as `needs-repro`. Bugs with the `needs-repro` tag will not be addressed
  until they are reproduced.
- If the issue can't be reproduced, it will be marked `needs-fix`, as well as possibly
  other tags (such as `critical`), and the issue will be left to
  be [implemented by someone](#your-first-code-contribution).

### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion for Contextium, **including
completely new features and minor improvements to existing functionality**. Following these
guidelines will help maintainers and the community to understand your suggestion and find related
suggestions.

#### Before Submitting an Enhancement

- Make sure that you are using the latest version.
- Read the [documentation](https://github.com/xcy7e/Contextium/blob/master/README.md) carefully and
  find out if the functionality is already covered, maybe by an individual configuration.
- Perform a [search](https://github.com/xcy7e/Contextium/issues) to see if the enhancement has
  already been suggested. If it has, add a comment to the existing issue instead of opening a new
  one.
- Find out whether your idea fits with the scope and aims of the project. It's up to you to make a
  strong case to convince the project's developers of the merits of this feature. Keep in mind that
  we want features that will be useful to the majority of our users and not just a small subset. If
  you're just targeting a minority of users, consider forking the project.

#### How Do I Submit a Good Enhancement Suggestion?

Enhancement suggestions are tracked as [GitHub issues](https://github.com/xcy7e/Contextium/issues).

- Use a **clear and descriptive title** for the issue to identify the suggestion.
- Provide a **step-by-step description of the suggested enhancement** in as many details as
  possible.
- **Describe the current behavior** and **explain which behavior you expected to see instead** and
  why. At this point you can also tell which alternatives do not work for you.
- You may want to **include screenshots or screen recordings** which help you demonstrate the steps
  or point out the part which the suggestion is related to. You can
  use [LICEcap](https://www.cockos.com/licecap/) to record GIFs on macOS and Windows, and the
  built-in [screen recorder in GNOME](https://help.gnome.org/users/gnome-help/stable/screen-shot-record.html.en)
  or [SimpleScreenRecorder](https://github.com/MaartenBaert/ssr) on
  Linux.
- **Explain why this enhancement would be useful** to most Contextium users. You may also want to
  point out the other projects that solved it better and which could serve as inspiration.

### Your First Code Contribution

Before working on a code contribution, make sure you are comfortable with the basic Android
development workflow, including Kotlin, Android Studio, Gradle, and Git. This guide assumes that you
can independently clone a repository, create a branch, build an Android project, and resolve basic
build or IDE issues.

#### Before you start

For non-trivial changes, please check the existing issues first. If no issue exists, consider
opening one before starting implementation, especially for new features, architectural changes, or
larger refactorings.

This helps avoid duplicate work and makes it possible to discuss the intended approach before
significant implementation effort is invested.

#### Set up the project

Fork the repository and create a dedicated branch for your change.

Open the project in a recent version of Android Studio and verify that the project builds
successfully before modifying anything.

Please familiarize yourself with the existing project structure, architecture, naming conventions,
and implementation patterns before introducing new abstractions or dependencies.

#### Make your change

Keep contributions focused on a single problem or feature.

Prefer changes that integrate with the existing architecture instead of introducing parallel
patterns or unnecessary abstractions.

Please avoid unrelated changes such as:

- large formatting-only modifications
- unrelated refactoring
- dependency upgrades that are not required by your change
- renaming or restructuring unrelated code
- generated IDE files or local configuration files

If your change introduces new behavior or modifies existing logic, add or update tests where
appropriate.

#### Verify your changes

Before opening a pull request:

- build the project successfully
- run the relevant tests (there are no tests yet)
- check the affected functionality on an emulator or physical device when applicable
- review your diff for accidental or unrelated changes
- make sure no secrets, local paths, credentials, or machine-specific files are included

Your pull request should clearly explain what was changed, why the change is necessary, and
reference the corresponding issue when one exists.

Small, well-scoped pull requests are significantly easier to review and are preferred over large
changes combining multiple unrelated concerns.

### Contextium Basic Knowledge

The structure of the app and specifically it's data is as followed.

#### The Data

- The **configurable Menu-Items** are defined in `MenuItem` (see also `ContextMenuItemDao`)

#### The Views (Activities)

- The **homescreen** is `ContextMenuItemManagerActivity`
- The **Add/Edit Item** is `ContextMenuItemSettingsActivity`
- The Screen where you can **pick items from selected text** is `ContextMenuItemPickerActivity`

#### Changes To The Structure Of Data And Data Migration

Every
a) change to the structure of any data model
b) extension or removal of data models

has to be backwards compatible. The user can **Import** and **Export** a backup json file.
On startup, the app must use the **migration layer** to bring data from the previous version into
the new, current structure. This migration is always from the very previous version to the current.
If a user lags multiple versions behind, when updating the app, every required migration step is
executed after on another.

### Improving The Documentation

When contributing new features, fixing bugs, or changing UI components, please check if your changes
affect the documentation. Make sure to update
the [README.md](https://github.com/xcy7e/Contextium/blob/master/README.md) (or relevant doc files)
accordingly. If
your changes affect the user interface or visual workflows, please update any existing screenshots
or add new ones to reflect the current state. Keeping docs and visuals accurate helps everyone!

## Styleguides

There are no specific code style guidelines in this project. Please follow the general coding style.

### Commit Messages

Please use conventional commits to describe your changes. If multiple changes are made, be sure to
separate them logically by making separate commits.
Use the [Conventional Commits-Guide](https://www.conventionalcommits.org/).

### Code Comments

Please add relevant comments to functions and logic. Not every line must be commented, but every
piece of logic should be explained in the shortest possible way.

---

## Attribution

This guide is based on the [contributing.md](https://contributing.md/generator)!
