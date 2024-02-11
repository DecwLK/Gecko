/**
 * Clusters classes that manage I/O file content. There are two types of operations provided:
 * <ol>
 *     <li>
 *         <b>Serialization</b>: Outlined by the {@link org.gecko.io.FileSerializer}, there are currently two types of
 *         serialization processes supported by Gecko.
 *         <ul>
 *             <li>
 *                 <b>Serialization to JSON</b>: The {@link org.gecko.viewmodel.SystemViewModel Root} of the model and
 *                 {@link org.gecko.io.ViewModelPropertiesContainer ViewModelProperties} of all view model elements are
 *                 serialized through Jackson, a suite of data-processing tools for Java, by the
 *                 {@link org.gecko.io.ProjectFileSerializer}, which uses a {@link org.gecko.io.ViewModelElementSaver}
 *                 to traverse all view model elements.
 *             </li>
 *             <li>
 *                 <b>Serialization to SYS</b>: Serializtaion to SYS files is done by the
 *                 {@link org.gecko.io.AutomatonFileSerializer AutmatonFileSerializer}.
 *                 When exporting the model it transforms features unique to Gecko such as
 *                 {@link org.gecko.model.Kind Kinds} or {@link org.gecko.model.Region Regions}
 *                 to be compatible with the SYS file format.
 *             </li>
 *         </ul>
 *     </li>
 *
 *     <li>
 *         <b>Parsing</b>: Outlined by the {@link org.gecko.io.FileParser}, there are currently two types of parsing
 *         processes supported by Gecko.
 *         <ul>
 *             <li>
 *                 <b>Parsing from JSON</b>: The {@link org.gecko.viewmodel.SystemViewModel Root} of the model and
 *                 {@link org.gecko.io.ViewModelPropertiesContainer ViewModelProperties} of all model elements are
 *                 parsed from a *.json file through Jackson by the {@link org.gecko.io.ProjectFileParser}, which uses a
 *                 {@link org.gecko.io.ViewModelElementCreator} to traverse the model and create the corresponding
 *                 view model elements.
 *             </li>
 *             <li>
 *                 <b>Parsing from SYS</b>: Parsing from SYS files is done by the
 *                 {@link org.gecko.io.AutomatonFileParser AutomatonFileParser}. It uses the
 *                 {@link org.gecko.io.AutomatonFileVisitor AutomatonFileVisitor},
 *                 which is an ANTLR4 visitor, to traverse the SYS file and create the corresponding model elements.
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 */
package org.gecko.io;