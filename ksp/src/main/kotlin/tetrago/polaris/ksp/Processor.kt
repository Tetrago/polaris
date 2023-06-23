package tetrago.polaris.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import tetrago.polaris.module.Polaris
import tetrago.polaris.module.Registry
import kotlin.reflect.KClass

class Processor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {
    private fun visitAnnotated(resolver: Resolver, annotation: KClass<*>, visitor: KSVisitorVoid): List<KSAnnotated> {
        val map = resolver.getSymbolsWithAnnotation(annotation.qualifiedName!!).associateWith { it.validate() }

        map.filter { it.value }.keys
            .filterIsInstance<KSClassDeclaration>()
            .forEach { it.accept(visitor, Unit) }

        return map.filterNot { it.value }.keys.toList()
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        return listOf(
            visitAnnotated(resolver, Registry::class, RegistryVisitor(logger, codeGenerator)),
            visitAnnotated(resolver, Polaris::class, ModuleVisitor(logger, codeGenerator))
        ).flatten()
    }
}

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(environment.logger, environment.codeGenerator)
    }
}