# Haxe4E Improvements and Suggestions

Based on a review of the `haxe4e` codebase, here are a number of bugs, potential improvements, and new features suggested for the project. The codebase provides a solid foundation for Haxe development in Eclipse, using LSP (Language Server Protocol) and TM4E (TextMate grammar) for core features.

## Bugs & Issues

1. **`HaxeEditor.java` - Marker Deletion Loop**
   In `performSave()`, the code iterates over diagnostic markers and deletes them manually:
   ```java
   for (final var marker : res.findMarkers(LSPDiagnosticsToMarkers.LS_DIAGNOSTIC_MARKER_TYPE, false, IResource.DEPTH_ONE)) {
      if ("org.haxe4e.langserv".equals(marker.getAttribute(LSPDiagnosticsToMarkers.LANGUAGE_SERVER_ID))) {
         marker.delete();
      }
   }
   ```
   *Issue*: Modifying the markers while potentially concurrently accessed by LSP4E might lead to issues. If this is a workaround for `vshaxe/vshaxe#507`, it should be periodically checked if the upstream bug is resolved, as deleting markers like this can cause flickers in the UI or race conditions with the Language Server trying to publish diagnostics.

2. **`HaxeEditor.java` - MouseDoubleClick Workaround**
   The workaround for `haxe4e/haxe4e#40` intercepts `mouseDoubleClick` to correctly select words containing colons (e.g., in type hints).
   *Issue*: This is slightly fragile and reliant on string manipulation (`selText.lastIndexOf(':')`). It would be more robust to configure the double-click strategy or the text partitioning to consider colons as part of a word when selecting, rather than hijacking the mouse listener. Eclipse's `DoubleClickStrategy` or modifying the document partitioner could be a cleaner approach.

3. **`HaxeFileSpellCheckingReconciler.java` - Spellchecking Concurrency**
   The reconciler uses a `Job` to perform spellchecking.
   *Issue*: If multiple rapid changes occur, jobs might queue up or cancel each other. The current implementation tracks a single `spellcheckJob` and cancels it, which is good. However, `annotationModelExt.replaceAnnotations()` inside `endCollecting()` is called from a background thread. While Eclipse's `IAnnotationModelExtension` is often thread-safe or handles UI sync internally, it's safer to ensure UI updates happen on the UI thread, or verify that `SPELLING_SERVICE.check` handles the thread synchronization correctly.

4. **`NewHaxeProjectTest.java` - Threading Extension**
   The `RunTestsOnNonUiThreadExtension` intercepts tests to run them off the UI thread because SWT tests usually need to run in a specific way.
   *Issue*: The custom threading extension uses a daemon thread and loops `display.readAndDispatch()`. While functional, this is a bit of a hack. Tycho Surefire and SWTBot have standard ways to handle UI thread testing (e.g., `@RunWith(SWTBotJunit4ClassRunner.class)` or JUnit 5 equivalents, configuring `useUIThread=false` in pom.xml, which is already done). If the test works without the extension in Tycho, the extension might only be necessary for running from the IDE. Consider documenting this clearly or using standard SWTBot annotations.

## Potential Improvements

1. **Leverage Modern Java Features**
   The project is configured to use Java 17. Ensure the codebase fully utilizes modern Java features such as `switch` expressions, text blocks, records, and pattern matching for `instanceof`, which can make the code more readable and concise.

2. **LSP Integration Refinement**
   The language server is registered via `plugin.xml` using `org.eclipse.lsp4e.languageServer`.
   *Improvement*: Ensure the `HaxeLangServerClientImpl` and related classes fully utilize the capabilities provided by LSP4E. Check if features like Code Actions (Quick Fixes), Rename, and Formatting are properly delegated to the LSP and not duplicated in the plugin code.

3. **Preference Page Organization**
   Currently, there are `HaxePreferencePage` and `HaxeSDKPreferencePage`.
   *Improvement*: As the plugin grows, organizing preferences into categories (e.g., Editor, Language Server, Formatter, SDKs) will make it easier for users to navigate. Consider moving specific formatter settings (if any) or LSP specific settings into their own sub-pages.

4. **Testing Coverage**
   The test directory contains `NewHaxeProjectTest.java` and `TreeBuilderTest.java`.
   *Improvement*: Increase test coverage, especially for core utility classes, the language server launcher logic, and model parsing. Adding headless JUnit tests for non-UI components will improve robustness.

5. **Code Formatting configuration**
   Haxe formatting relies on `haxe-formatter`.
   *Improvement*: Ensure the formatting settings configured in `haxe_formatter.json` can be managed or at least recognized by the Eclipse plugin. A preference page to define the location of the formatter config or to override basic settings would be useful.

## Suggested New Features

1. **Haxelib Integration**
   *Feature*: Add a dedicated view or UI for managing `haxelib` dependencies. Currently, dependencies are updated via `HaxeDependenciesUpdater`, but a UI to search, install, update, and remove libraries directly from Eclipse would be a significant productivity boost. This could be integrated into the Project Properties or a separate "Haxelib View".

2. **Project Structure / Natures**
   *Feature*: Provide specific project templates (e.g., Console App, OpenFL project, Kha project) in the New Project Wizard. This would set up the initial `hxml` file and directory structure automatically based on the chosen template.

3. **Advanced Build Configuration**
   *Feature*: Enhance the `HaxeBuilder` to parse `hxml` files and provide a UI editor for them (like the generic editor, but with form-based fields for common flags). Add support for multiple build targets within a single project, allowing users to switch easily between building for HashLink, JS, Neko, etc., directly from a toolbar dropdown.

4. **Debugging Enhancements**
   *Feature*: While `haxe eval` debugging is mentioned in the README, integrating with specific target debuggers (e.g., HashLink debugger or Chrome/Node debugger for JS targets) using LSP/DAP (Debug Adapter Protocol) would greatly enhance the development experience. This might involve creating specific launch configurations for different targets.

5. **Code Snippets and Templates**
   *Feature*: Provide a rich set of built-in code snippets for common Haxe constructs (e.g., `class`, `interface`, `typedef`, `for`, `switch`) using Eclipse's template engine, in addition to the TM4E snippets.
