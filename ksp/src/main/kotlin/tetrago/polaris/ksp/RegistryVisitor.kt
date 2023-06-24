package tetrago.polaris.ksp

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import tetrago.polaris.module.Holder
import tetrago.polaris.module.Registry

class RegistryVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) : KSVisitorVoid() {
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        if(classDeclaration.classKind != ClassKind.OBJECT) {
            logger.error("Only objects can be annotated with `Registry`")
            return
        }

        val name = classDeclaration.simpleName.asString()
        if(!name.endsWith("Registry")) {
            error("Registry object names must be suffixed by `Registry`")
        }

        val registryAnnotation = classDeclaration.annotations
            .find { it.shortName.asString() == Registry::class.simpleName }!!
        val entityType = registryAnnotation.arguments.first().value as KSType

        val className = ClassName(
            classDeclaration.packageName.asString(),
            name.replace("Registry", "Holder")
        )

        logger.info("Building class `${className.simpleName}")
        val holder = TypeSpec.objectBuilder(className)

        classDeclaration.getDeclaredProperties().forEach { property ->
            val holderAnnotation = property.annotations
                .find { it.shortName.asString() == Holder::class.simpleName } ?: return@forEach

            val value = holderAnnotation.arguments.first().value.let { it as String }
                .ifEmpty { property.simpleName.asString() }

            logger.info("Writing holder property `${property.simpleName.asString()}`")
            holder.addProperty(
                PropertySpec.builder(property.simpleName.asString(), property.type.toTypeName())
                    .getter(FunSpec.getterBuilder()
                        .addCode("return %T.find { %T.name eq %S }.single()",
                            property.type.toTypeName(), entityType.toTypeName(), value)
                        .build())
                    .build())
        }

        val file = FileSpec.builder(className)
            .addType(holder.build())
            .build()

        file.writeTo(codeGenerator, Dependencies(true))
    }
}
