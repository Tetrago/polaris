package tetrago.polaris.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
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
            visitAnnotated(resolver, Polaris::class, ModuleVisitor(logger, codeGenerator)),
            visitAnnotated(resolver, Registry::class, RegistryVisitor(logger, codeGenerator))
        ).distinct().flatten()
    }
}

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(environment.logger, environment.codeGenerator)
    }
}
